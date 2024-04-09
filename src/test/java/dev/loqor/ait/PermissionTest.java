package dev.loqor.ait;

import loqor.ait.tardis.data.permissions.Permission;
import loqor.ait.tardis.data.permissions.PermissionMap;

import static dev.loqor.ait.Test.assertEquals;

public class PermissionTest {

    public static void main(String[] args) {
        PermissionMap map = new PermissionMap();

        map.set(Permission.USE.TRAVEL, true);
        map.set(Permission.USE.ATTUNE, false);

        assertEquals(map.get(Permission.USE.ATTUNE), false);
        assertEquals(map.get(Permission.USE.TRAVEL), true);

        assertEquals(Permission.from("tardis.use.travel"), Permission.USE.TRAVEL);
    }
}
