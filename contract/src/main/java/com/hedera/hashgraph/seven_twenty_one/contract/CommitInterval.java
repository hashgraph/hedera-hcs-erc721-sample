package com.hedera.hashgraph.seven_twenty_one.contract;

import com.hedera.hashgraph.seven_twenty_one.contract.repository.TransactionRepository;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.Instant;
import javax.annotation.Nullable;

public final class CommitInterval {

    private final int intervalMs;

    private final State state;

    private final TransactionRepository transactionRepository;

    @Nullable
    private Instant lastCommitTime;

    public CommitInterval(
        Dotenv env,
        State state,
        TransactionRepository transactionRepository
    ) {
        this.state = state;
        this.transactionRepository = transactionRepository;

        intervalMs =
            Integer.parseInt(env.get("H721_COMMIT_INTERVAL", "1")) * 1000;

        new Thread(this::onInterval).start();
    }

    private void onInterval() {
        while (true) {
            try {
                // noinspection BusyWait
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                break;
            }

            state.lock();

            try {
                if (state.getTimestamp() == null) {
                    // nothing has happened (yet)
                    return;
                }

                if (
                    lastCommitTime != null &&
                    lastCommitTime.equals(state.getTimestamp())
                ) {
                    // we just wrote this state
                    // nothing has been happening
                    return;
                }

                transactionRepository.execute();

                lastCommitTime = state.getTimestamp();
            } finally {
                state.unlock();
            }
        }
    }
}
