package io.github.encryptorcode.implementation.storage.jdbc.tables;

import org.jooq.Name;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * An auto-generated class that helps make JOOQ implementation easier
 */
public class USERS extends TableImpl<USERS.USERS_RECORD> {

    public static final USERS USERS = new USERS();

    public final TableField<USERS_RECORD, String> ID =
            createField(DSL.name("id"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<USERS_RECORD, String> NAME =
            createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<USERS_RECORD, String> FULL_NAME =
            createField(DSL.name("full_name"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<USERS_RECORD, String> EMAIL =
            createField(DSL.name("email"), SQLDataType.VARCHAR(255).nullable(true), this, "");
    public final TableField<USERS_RECORD, String> PROFILE_IMAGE =
            createField(DSL.name("profile_image"), SQLDataType.VARCHAR(255).nullable(true), this, "");

    public USERS() {
        this(DSL.name("users"), USERS);
    }

    public USERS(String alias) {
        this(DSL.name(alias), USERS);
    }

    public USERS(Name alias) {
        this(alias, USERS);
    }

    public USERS(Name alias, TableImpl<USERS_RECORD> aliased) {
        super(alias, null, aliased);
    }

    public USERS as(String alias) {
        return new USERS(DSL.name(alias), this);
    }

    public USERS as(Name alias) {
        return new USERS(alias, this);
    }

    public static class USERS_RECORD extends UpdatableRecordImpl<USERS_RECORD> {
        public USERS_RECORD(Table<USERS_RECORD> table) {
            super(table);
        }
    }
}
