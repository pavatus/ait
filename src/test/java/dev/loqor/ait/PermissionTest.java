package dev.loqor.ait;

import loqor.ait.tardis.data.permissions.Permission;
import loqor.ait.tardis.data.permissions.PermissionMap;

import static dev.loqor.ait.Test.assertEquals;

public class PermissionTest {

    public static void main(String[] args) {
        PermissionMap map = new PermissionMap();

        map.add(Permission.USE.TRAVEL);
        map.add(Permission.USE.LINK);

        assertEquals(map.contains(Permission.USE.LINK), false);
        assertEquals(map.contains(Permission.USE.TRAVEL), true);

        assertEquals(Permission.from("tardis.use.travel"), Permission.USE.TRAVEL);
    }
}
