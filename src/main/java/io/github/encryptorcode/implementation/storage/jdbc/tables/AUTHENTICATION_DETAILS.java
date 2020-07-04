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
public class AUTHENTICATION_DETAILS extends TableImpl<AUTHENTICATION_DETAILS.AUTHENTICATION_DETAILS_RECORD> {

    public static final AUTHENTICATION_DETAILS AUTHENTICATION_DETAILS = new AUTHENTICATION_DETAILS();

    public final TableField<AUTHENTICATION_DETAILS_RECORD, String> USER_ID =
            createField(DSL.name("user_id"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<AUTHENTICATION_DETAILS_RECORD, String> PROVIDER =
            createField(DSL.name("provider"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<AUTHENTICATION_DETAILS_RECORD, String> PROVIDED_USER_ID =
            createField(DSL.name("provided_user_id"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<AUTHENTICATION_DETAILS_RECORD, String> ACCESS_TOKEN =
            createField(DSL.name("access_token"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<AUTHENTICATION_DETAILS_RECORD, ZonedDateTime> EXPIRY_TIME =
            createField(DSL.name("expiry_time"), SQLDataType.BIGINT.nullable(true), this, "", ZonedDateTimeConvertor.instance());
    public final TableField<AUTHENTICATION_DETAILS_RECORD, String> REFRESH_TOKEN =
            createField(DSL.name("refresh_token"), SQLDataType.VARCHAR(255).nullable(true), this, "");

    public AUTHENTICATION_DETAILS() {
        this(DSL.name("authentication_details"), AUTHENTICATION_DETAILS);
    }

    public AUTHENTICATION_DETAILS(String alias) {
        this(DSL.name(alias), AUTHENTICATION_DETAILS);
    }

    public AUTHENTICATION_DETAILS(Name alias) {
        this(alias, AUTHENTICATION_DETAILS);
    }

    public AUTHENTICATION_DETAILS(Name alias, TableImpl<AUTHENTICATION_DETAILS_RECORD> aliased) {
        super(alias, null, aliased);
    }

    public AUTHENTICATION_DETAILS as(String alias) {
        return new AUTHENTICATION_DETAILS(DSL.name(alias), this);
    }

    public AUTHENTICATION_DETAILS as(Name alias) {
        return new AUTHENTICATION_DETAILS(alias, this);
    }

    public static class AUTHENTICATION_DETAILS_RECORD extends UpdatableRecordImpl<AUTHENTICATION_DETAILS_RECORD> {
        public AUTHENTICATION_DETAILS_RECORD(Table<AUTHENTICATION_DETAILS_RECORD> table) {
            super(table);
        }
    }
}
