package works.lysenko.utils;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"SerializableHasSerializationMethods", "ClassIndependentOfModule", "ClassWithoutNoArgConstructor", "UncheckedExceptionClass", "WeakerAccess"})
public class KnownIssueException extends RuntimeException {

    private static final long serialVersionUID = 4203658749164177761L;

    /**
     * @param message message to identify the known issue
     */
    @SuppressWarnings({"unused", "PublicConstructor"})
    public KnownIssueException(String message) {
        super(message);
    }

}
