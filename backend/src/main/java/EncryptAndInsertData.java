/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


// [START cloud_sql_mysql_cse_insert]

import com.google.crypto.tink.Aead;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.sql.DataSource;

public class EncryptAndInsertData {

  public static void main(String[] args) throws GeneralSecurityException, SQLException {
    // Saving credentials in environment variables is convenient, but not secure - consider a more
    // secure solution such as Cloud Secret Manager to help keep secrets safe.
    String dbUser = System.getenv("DB_USER"); // e.g. "root", "mysql"
    String dbPass = System.getenv("DB_PASS"); // e.g. "mysupersecretpassword"
    String dbName = System.getenv("DB_NAME"); // e.g. "votes_db"
    String instanceConnectionName =
        System.getenv("INSTANCE_CONNECTION_NAME"); // e.g. "project-name:region:instance-name"
    String kmsUri = System.getenv("CLOUD_KMS_URI"); // e.g. "gcp-kms://projects/...path/to/key
    // Tink uses the "gcp-kms://" prefix for paths to keys stored in Google Cloud KMS. For more
    // info on creating a KMS key and getting its path, see
    // https://cloud.google.com/kms/docs/quickstart

    String userName = "EEE";
    String tableName = "users";
    String email = "ee@gmail.com";
    String description = "This is EEE.";

    // Initialize database connection pool and create table if it does not exist
    // See CloudSqlConnectionPool.java for setup details
    DataSource pool =
        CloudSqlConnectionPool.createConnectionPool(dbUser, dbPass, dbName, instanceConnectionName);
    // CloudSqlConnectionPool.createTable(pool, tableName);

    // Initialize envelope AEAD
    // See CloudKmsEnvelopeAead.java for setup details
    Aead envAead = CloudKmsEnvelopeAead.get(kmsUri);

    encryptAndInsertData(pool, envAead, tableName, userName, email, description);
  }

  public static void encryptAndInsertData(
      DataSource pool, Aead envAead, String tableName, String userName, String email, String description)
      throws GeneralSecurityException, SQLException {

    try (Connection conn = pool.getConnection()) {
      String stmt =
          String.format(
              "INSERT INTO %s (user_name, email, description) VALUES (?, ?, ?);", tableName);
      try (PreparedStatement userStmt = conn.prepareStatement(stmt) ) {
        userStmt.setString(1, userName);
        //userStmt.setString(2, email);
        userStmt.setString(3, description);


        // Use the envelope AEAD primitive to encrypt the email, using the description, user_name as
        // associated data. This binds the encryption of the email to the iser name, preventing
        // associating an encrypted email in one row with a team name in another row.
        byte[] encryptedEmail = envAead.encrypt(description.getBytes(), userName.getBytes());
        userStmt.setBytes(2, encryptedEmail);

        // Finally, execute the statement. If it fails, an error will be thrown.
        userStmt.execute();
        System.out.println(String.format("Successfully inserted row into table %s", tableName));
      }
    }
  }
}
// [END cloud_sql_mysql_cse_insert]
