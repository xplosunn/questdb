/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2020 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.cairo.sql;

import io.questdb.griffin.SqlExecutionContext;
import io.questdb.std.Sinkable;
import io.questdb.std.str.CharSink;

import java.io.Closeable;

/**
 * Factory for creating a SQL execution plan.
 * Queries may be executed more than once without changing execution plan.
 *
 * Interfaces which extend Closeable are not optionally-closeable.
 * close() method must be called after other calls are complete.
 *
 * Example:
 *
 * final SqlExecutionContextImpl ctx = new SqlExecutionContextImpl(engine, 1);
 * try (SqlCompiler compiler = new SqlCompiler(engine)) {
 *     try (RecordCursorFactory factory = compiler.compile("abc", ctx).getRecordCursorFactory()) {
 *         try (RecordCursor cursor = factory.getCursor(ctx)) {
 *             final Record record = cursor.getRecord();
 *             while (cursor.hasNext()) {
 *                 // access 'record' instance for field values
 *             }
 *         }
 *     }
 * }
 *
 */
public interface RecordCursorFactory extends Closeable, Sinkable {
    @Override
    default void close() {
    }

    default boolean followedOrderByAdvice() {
        return false;
    }

    /**
     * @param executionContext name of a SQL execution context
     * @return
     */
    RecordCursor getCursor(SqlExecutionContext executionContext);

    RecordMetadata getMetadata();

    default PageFrameCursor getPageFrameCursor(SqlExecutionContext executionContext) {
        return null;
    }

    boolean recordCursorSupportsRandomAccess();

    default boolean supportPageFrameCursor() {
        return false;
    }

    default void toSink(CharSink sink) {
        throw new UnsupportedOperationException();
    }
}
