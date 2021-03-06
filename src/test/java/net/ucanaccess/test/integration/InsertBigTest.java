/*
Copyright (c) 2012 Marco Amadei.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package net.ucanaccess.test.integration;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import net.ucanaccess.test.util.AccessVersion;
import net.ucanaccess.test.util.AccessVersionAllTest;

@RunWith(Parameterized.class)
public class InsertBigTest extends AccessVersionAllTest {

    public InsertBigTest(AccessVersion _accessVersion) {
        super(_accessVersion);
    }

    @Before
    public void beforeTestCase() throws Exception {
        executeStatements("CREATE TABLE Tbig (id LONG, descr MEMO)");
    }

    @Test
    public void testBig() throws SQLException, IOException {
        Statement st = null;
        st = ucanaccess.createStatement();
        int id = 6666554;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append(String.format("%05d", i));
            sb.append("\r\n");
        }
        String s = sb.toString();
        assertTrue(s.length() >= 65536);
        st.execute("INSERT INTO Tbig (id,descr)  VALUES( " + id + ",'" + s + "')");
        ResultSet rs = st.executeQuery("SELECT descr FROM Tbig WHERE id=" + id);
        rs.next();
        String retrieved = rs.getString(1);
        assertEquals(s, retrieved);
        st.close();
    }

}
