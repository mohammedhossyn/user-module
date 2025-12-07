package com.usermodule.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
@RequiredArgsConstructor
public class TransactionUtil {

    private final PlatformTransactionManager transactionManager;
    private final ThreadLocal<TransactionStatus> threadLocalStatus = new ThreadLocal<>();

    public void openTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        threadLocalStatus.set(status);
    }

    public void commit() {
        if (threadLocalStatus.get() != null) {
            transactionManager.commit(threadLocalStatus.get());
            threadLocalStatus.set(null);
        }
    }

    public void rollback() {
        if (threadLocalStatus.get() != null) {
            transactionManager.rollback(threadLocalStatus.get());
            threadLocalStatus.set(null);
        }
    }

    public boolean isOpenTrans() {
        return threadLocalStatus.get() != null && !threadLocalStatus.get().isCompleted();
    }
}
