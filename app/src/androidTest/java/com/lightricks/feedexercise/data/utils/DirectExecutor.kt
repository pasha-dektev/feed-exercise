
package com.lightricks.feedexercise.data.utils

import java.util.concurrent.Executor

object DirectExecutor : Executor {
    override fun execute(command: Runnable) {
        command.run()
    }
}
