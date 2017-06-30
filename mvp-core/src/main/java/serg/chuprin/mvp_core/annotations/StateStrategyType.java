package serg.chuprin.mvp_core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import serg.chuprin.mvp_core.viewstate.strategy.AddToEndSingleOneExecutionStrategy;
import serg.chuprin.mvp_core.viewstate.strategy.StateStrategy;

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface StateStrategyType {
    Class<? extends StateStrategy> value() default AddToEndSingleOneExecutionStrategy.class;
}
