package com.goodworkalan.paste.servlet;

// TODO Document.
class PasteControllerException extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    // TODO Document.
    private final int depth;
    
    // TODO Document.
    public PasteControllerException(Throwable cause, int depth) {
        super("Controller threw an exception.", cause);
        this.depth = depth;
    }
    
    // TODO Document.
    public Throwable getControllerException() {
        Throwable throwable = this;
        for (int i = 0; i < depth; i++) {
            throwable = throwable.getCause();
        }
        return throwable;
    }
}
