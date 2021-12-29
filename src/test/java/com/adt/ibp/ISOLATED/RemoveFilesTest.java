package com.adt.ibp.ISOLATED;

import com.adt.ibp.Utils.DeleteFilesFromDir;

public class RemoveFilesTest {
    public static void main(String[] args) {
        DeleteFilesFromDir deleteFiles = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles", ".json");
    }
}
