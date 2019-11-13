# MvpCore
MvpCore is a tiny library to setup MVP pattern in your app, based on Dagger 2.

# Setup

Latest version is **1.0.7**

First add repository to your project's *build.gradle* 
```groovy
allprojects {
    repositories {
        jcenter()
    }
}
```
Add dependencies to your app's *build.gradle* 
```groovy
dependencies {
   
    compile 'serg.chuprin:mvp-core:$latestVersion'
    compile 'serg.chuprin:mvp-core-android:$latestVersion'
    kapt 'serg.chuprin:mvp-core-processor:$latestVersion'
    
    // As far as lib based on Dagger components caching, you should include dagger's dependencies
    compile 'com.google.dagger:dagger:$latestVersion'
    kapt 'com.google.dagger:dagger-compiler:$latestVersion'
}
```

# How it works
Library is based on dagger's components caching. So all dependencies (presenters) are retained across configuration change.
You don't need to manually inject library, it is done automatically via reflection.

## ViewState
Have you faced with a case when response comes from a server, but view is in the background and the response is lost?
No more! MvpCore uses command's queue so if event comes when view is not visible to user, the event will be placed 
in queue and executed after view becomes visible to user.

To use it, add **InjectViewState** annotation to your presenter.
If you don't want it, don't do it. In this case you no need to check view for null, because MvpCore uses NullObject pattern;

Similarly to **Moxy** (https://github.com/Arello-Mobile/Moxy) there are different strategies to manage view commands.
* AddToEndSingleOneExecutionStrategy (by default)
* 
  Command will be added in queue once (queue might contain only one command) and removed after execution
* AddToEndSingleStrategy 
* 
  Command will be added in queue once (queue might contain only one command)
* SingleStateStrategy 
* 
  Queue will be cleared before command is added, so only one command will be present in queue
* SkipStrategy 
* 
  Command will not be added in queue
 
 You can annotate the whole view interface with specific strategy or annotate concrete methods.
  ```kotlin
@StateStrategyType(value = SingleStateStrategy::class)
interface UserView : MvpView {

    fun showReposCount(count: String)
    
    @StateStrategyType(value = SkipStrategy::class)
    fun showRepositories(repositories: List<GithubRepositoryEntity>)
}
 ```
 And of course, feel free to add your own strategies. You need to extend **StateStrategy**.
 
 ```kotlin
 class CustomStrategy : StateStrategy {
 
    override fun <V : MvpView?> beforeExecute(currentCommands: Queue<ViewCommand<V>>?, command: ViewCommand<V>?) = Unit

    override fun <V : MvpView?> afterExecute(currentCommands: Queue<ViewCommand<V>>?, command: ViewCommand<V>?) = Unit
}
```

## Lifecycle
View is attached in *onStart* method and detached in *onStop* method.

# How to

* Create *AppComponent* and initialize it in Application class
 
```kotlin
@Component(modules = [/*your modules*/])
@Singleton
interface AppComponent {

    //You need to add every subcomponent in root component (AppComponent).

    fun userComponent(module: UserModule): UserComponent
}
```

```kotlin
class YourApplication : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().build()
    }
}
```

* Create custom scope

```kotlin
@Scope
@Retention(RetentionPolicy.RUNTIME)
@annotation class PerView
```

* Create module and subcomponent for your feature.

Module:

```kotlin
@Module class UserModule {

    @Provides
    @PerView
    fun providePresenter(): UserPresenter = UserPresenter()
}
```
Subcomponent:

```kotlin
@Subcomponent(modules = arrayOf(UserModule::class))
@PerView
interface UserComponent {
    fun inject(activity: UserActivity)
}
```

* Create presenter
 
```kotlin
class UserPresenter @Inject constructor(
/*your injected dependencies*/
): MvpPresenter<UserView>
```

* Create view interface

```kotlin
interface UserView: MvpView
```

* Create activity/fragment which implements this interface and overrides 
**createComponent** and **componentClass** methods

```kotlin
class UserActivity : MvpActivity<UserPresenter>(), UserView {

    override fun createComponent(): Any {
        return YourApplication.component.userComponent(UserModule())
    }

    override fun componentClass(): Class<*> = UserComponent::class.java
}
```

Done!