package de.daver.unihub.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @version 1.0.0
 * Use the Annotation to create a subcommand in a
 * {@link Command} annotated class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    String name();
    String perm();
    boolean player();

}
