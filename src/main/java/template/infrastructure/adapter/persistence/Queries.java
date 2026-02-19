package template.infrastructure.adapter.persistence;

public class Queries {

    public static final String MERGE_QUERY = "MERGE INTO item (id, name) KEY(id) VALUES (?, ?)";

    public static final String LOCK_QUERY = "SELECT 1 FROM ITEM_SEQ_LOCK FOR UPDATE";

    public static final String ALTER_SEQUENCE_QUERY = "ALTER SEQUENCE ITEM_SEQ RESTART WITH %d";

    public static final String CURRENT_SEQ_VAL_QUERY = "SELECT CAST(BASE_VALUE AS BIGINT) FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_NAME = 'ITEM_SEQ'";

}
