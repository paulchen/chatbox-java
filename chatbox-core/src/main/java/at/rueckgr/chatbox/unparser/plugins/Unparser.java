package at.rueckgr.chatbox.unparser.plugins;

/**
 * @author paulchen
 */
public @interface Unparser {
    int order() default 0;
}
