package de.daver.unihub.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 * Annotation to set the description for a subcommand
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommandDescription {

    String[] description();

}
