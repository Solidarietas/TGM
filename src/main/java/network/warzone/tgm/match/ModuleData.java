package network.warzone.tgm.match;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleData {

    /**
     * @return The position of when the module should be loaded
     */
    ModuleLoadTime load() default ModuleLoadTime.NORMAL;

}
