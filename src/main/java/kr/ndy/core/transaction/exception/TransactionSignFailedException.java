package kr.ndy.core.transaction.exception;

public class TransactionSignFailedException extends RuntimeException {

    public TransactionSignFailedException(String message) {
        super(message);
    }
}
