package de.daver.unihub.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 * Allows a SubCommand to have multiple names
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommandAlias {

    String[] alias();

}
