package com.adt.ibp.ISOLATED;

import com.adt.ibp.Utils.DeleteFilesFromDir;
import org.testng.annotations.Test;

public class QuickUnitTests {
    @Test
    public void removeFilesMethodUnitTest(){
        DeleteFilesFromDir remove_cameraStatusList = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles/cameraStatusList.json");
        DeleteFilesFromDir remove_ibpConfig = new DeleteFilesFromDir("src/test/resources/DynamicValueFiles/ibpConfig.json");
    }
}
