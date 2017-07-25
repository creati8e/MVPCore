# MvpCore
MvpCore is a tiny library to setup Mvp pattern in your app, based on Dagger 2 and powered with RxJava.

# Setup

Latest version is **1.0.1**

Firstly add repository to your project's *build.gradle* 
```groovy
allprojects {
    repositories {
        ...
        maven { url "http://dl.bintray.com/creati8e/maven" }
    }
}
```
Add dependencies to your app's *build.gradle* 
```groovy
dependencies {
    ...

    compile 'serg.chuprin:mvp-core:1.0.1'
    compile 'serg.chuprin:mvp-core-android:1.0.1'
    apt 'serg.chuprin:mvp-core-processor:1.0.1'
    
    // As far as lib based on Dagger components caching, you should include dagger's dependencies
    compile 'com.google.dagger:dagger:2.10'
    apt 'com.google.dagger:dagger-compiler:2.10'
}
```

If you want to use MvpCore with Kotlin, use *kapt* instead of *apt*.
Also you should add these lines

```groovy
android {
    ...
    kapt {
        generateStubs = true
    }
}
```
# How it works
Library based on dagger's components caching. So all dependencies (presenters) are retained across configuration change.
You no need to manually inject, library do it automatically. This done via reflection.

## ViewState
Do you faced with a case when response comes from a server, but view in the background and this response is lost?
No more! MvpCore using command's queue so if event come when view is not visible to user, this event will be placed 
in queue and executed after view became visible to user.

To use it, add **InjectViewState** annotation to your presenter.
If you do not want, don't do it. In this case you no need to check view for null, because MvpCore uses NullObject pattern;

Similarly to **Moxy** (https://github.com/Arello-Mobile/Moxy) there are different strategies to manage view commands.
* AddToEndSingleOneExecutionStrategy (by default)
* 
  Command will be added in queue once (queue might contains only one such command) and removed after execution
* AddToEndSingleStrategy 
* 
  Command will be added in queue once (queue might contains only one such command)
* SingleStateStrategy 
* 
  Queue will be cleared before command added, so only one command will be present in queue
* SkipStrategy 
* 
  Command will not be added in queue
 
 Yoy can annotate the whole view interface with specific strategy or annotate concrete methods.
  ```kotlin
@StateStrategyType(value = SingleStateStrategy::class)
interface UserView : MvpView {

    fun showReposCount(count: String)
    
    @StateStrategyType(value = SkipStrategy::class)
    fun showRepositories(repositories: List<GithubRepositoryEntity>)
}
 ```
 And of course, feel free to add your own strategies. You need to extends from **StateStrategy**.
 
 ```kotlin
 class CustomStrategy : StateStrategy {
 
    override fun <V : MvpView?> beforeExecute(currentCommands: Queue<ViewCommand<V>>?, command: ViewCommand<V>?) = Unit

    override fun <V : MvpView?> afterExecute(currentCommands: Queue<ViewCommand<V>>?, command: ViewCommand<V>?) = Unit
}
```

## Lifecycle
View attached in *onStart* method and detached in *onStop* method.

## RxJava support

You can use RxJava 1 or 2. 
Use *subscribeView* method in presenter. Subscription list will be cleared when view detached.

Similarly you can use *addSubscription* method in MvpActivty/MvpFragment. Subscription list will be cleared in *onStop*.

# How to

1. As usually, create *AppComponent* and initialize it in Application class
```kotlin
@Component(modules = arrayOf(/*your modules*/))
@Singleton
interface AppComponent {

    //You need to add every subcomponent in root component (AppComponent).

    fun userComponent(module: UserModule): UserComponent
}
```

```kotlin
class YourAppication : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().build()
    }
}
```

2. Create custom scope

```kotlin
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerView {
}
```
3. Create module and subcomponent for your feature.

Module:
```kotlin
@Module class UserModule() {

    @Provides
    @PerView
    fun providePresenter(): UserPresenter {
        return UserPresenter()
    }
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
4. Create presenter
 
```kotlin
class UserPresenter @Inject constructor(/*your injected dependencies*/): MvpPresenter<UserView> {
}
```
5. Create view interface

```kotlin
interface UserView: MvpView
```
6. Create activity/fragment which implements this interface and overrides **createComponent** and **componentClass** methods

```kotlin
class UserActivity : MvpActivity<UserPresenter>(), UserView {

    override fun createComponent(): Any {
        return YourApplication.component.userComponent(UserModule())
    }

    override fun componentClass(): Class<*> = UserComponent::class.java
}
```

Done!


