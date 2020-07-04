package io.github.encryptorcode.implementation.storage.jdbc.tables;

import io.github.encryptorcode.implementation.storage.jdbc.converters.ZonedDateTimeConvertor;
import org.jooq.Name;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;

import java.time.ZonedDateTime;

/**
 * An auto-generated class that helps make JOOQ implementation easier
 */
public class SESSIONS extends TableImpl<SESSIONS.SESSIONS_RECORD> {

    public static final SESSIONS SESSIONS = new SESSIONS();

    public final TableField<SESSIONS_RECORD, String> IDENTIFIER =
            createField(DSL.name("identifier"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<SESSIONS_RECORD, String> USER_ID =
            createField(DSL.name("user_id"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<SESSIONS_RECORD, String> PROVIDER_ID =
            createField(DSL.name("provider_id"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<SESSIONS_RECORD, ZonedDateTime> CREATION_TIME =
            createField(DSL.name("creation_time"), SQLDataType.BIGINT.nullable(true), this, "", ZonedDateTimeConvertor.instance());
    public final TableField<SESSIONS_RECORD, ZonedDateTime> EXPIRY_TIME =
            createField(DSL.name("expiry_time"), SQLDataType.BIGINT.nullable(true), this, "", ZonedDateTimeConvertor.instance());

    public SESSIONS() {
        this(DSL.name("sessions"), SESSIONS);
    }

    public SESSIONS(String alias) {
        this(DSL.name(alias), SESSIONS);
    }

    public SESSIONS(Name alias) {
        this(alias, SESSIONS);
    }

    public SESSIONS(Name alias, TableImpl<SESSIONS_RECORD> aliased) {
        super(alias, null, aliased);
    }

    public SESSIONS as(String alias) {
        return new SESSIONS(DSL.name(alias), this);
    }

    public SESSIONS as(Name alias) {
        return new SESSIONS(alias, this);
    }

    public static class SESSIONS_RECORD extends UpdatableRecordImpl<SESSIONS_RECORD> {
        public SESSIONS_RECORD(Table<SESSIONS_RECORD> table) {
            super(table);
        }
    }
}
