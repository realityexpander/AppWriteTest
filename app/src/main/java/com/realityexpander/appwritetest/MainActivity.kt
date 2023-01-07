package com.realityexpander.appwritetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.realityexpander.appwritetest.ui.theme.AppWriteTestTheme
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.gson
import io.appwrite.extensions.toJson
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Functions
import io.appwrite.services.Realtime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

// For use with AppWrite
// https://appwrite.io/

// 1. Start the docker container
// docker run -it --rm -p 80:80 -p 443:443 -v /var/run/docker.sock:/var/run/docker.sock appwrite/appwrite

// 2. Go to http://localhost to see the AppWrite dashboard
// 3. Create a new project
// 4. Create a new collection
// 5. Create a new document
// 6. Create a new API key
// 7. Create a new user

// X. Stop the docker container
// docker stop $(docker ps -a -q)

// Show running containers
// docker ps

// Logs from a docker container:
// docker logs <container id>

// Create a new project
// appwrite init project

// Check status of client
// appwrite client --debug


// Create document command line
// appwrite databases createDocument --databaseId test2 --collectionId freight  --documentId 'unique()' --data '{ "Name": "truck 2" }' --permissions 'read("any")' 'write("any")'

// List documents command line
// appwrite databases listDocuments --databaseId test2 --collectionId freight

// For all logs, use: docker-entrypoint Image Container_ID

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val appWriteClient = Client(this)
//                .setEndpoint("http://192.168.0.186:80/v1") // Your API Endpoint
                .setEndpoint("http://6905-187-226-98-29.ngrok.io/v1") // Your API Endpoint
                .setProject("app-one") // Your project ID
                .setSelfSigned(true)
//                .addHeader("X-Appwrite-Key", "96bf3a05a5c5593d8d6ac97ffe38b7c3ede02268ef4a09ee95397500c8a389db1d5a8024f0f1fe11caed924ed5903a217b0dc9eb6273d947fe5699b21362d5deed9e5bce58f7a05316d8f4bdb7ca4686e741d2fd87828b21ec6274b4b7869778a12fb818a7508ebfc2d38173adf85bdbb1a4da830e121868d30cc30d1704cbd7")
                .setLocale("en-US")



            // Subscribe to database channel
            val realtime = Realtime(appWriteClient)
            val subscription = realtime.subscribe("documents") {
                if (it.events.contains("databases.*.collections.*.documents.*.create")) {
                    // Log when a new file is uploaded
                    println("payload: ${it.payload}")
                }
            }

            val database = Databases(appWriteClient);
            val account = Account(appWriteClient)
            lifecycleScope.launch {

//                delay(1000)
//                try {
//                    val document = database.createDocument(
////                        databaseId = "63b4fdd5b9a25c43a209",
//                        databaseId = "test2",
//                        collectionId = "freight",
//                        documentId = UUID.randomUUID().toString(), //ID.unique(),
//    //                    data = gson.toJson(mapOf("name" to "John Doe + ${UUID.randomUUID()}"))
//                        data = mapOf("Name" to "John Doe + ${UUID.randomUUID()}"),
//    //                    permissions = listOf(
//    //                        Permission.read(Role.users()),
//    //                        Permission.update(Role.users()),
//    //                        Permission.delete(Role.users()),
//    //                    ),
//                        permissions = listOf(
//                            Permission.read(Role.any()),
//                            Permission.update(Role.any()),
//                            Permission.delete(Role.any()),
//                        ),
//                    )
//
//                    print(document.toJson())
//                } catch (e: AppwriteException) {
//                    print("${e.message}, ${e.code}, ${e.response}")
//                }

                println("database: $database")

//                // Login
//                delay(1000)
//                try {
//                    val document = account.createEmailSession(
//                        email="chris.athanas.now@gmail.com",
//                        password="Zapper2041",
//                    )
//
//                    print(document.toJson())
//                } catch (e: AppwriteException) {
//                    print("${e.message}, ${e.code}, ${e.response}")
//                }

                // Trigger Function
                delay(1000)
                try {
                    val functions = Functions(appWriteClient)

                    val response = functions.createExecution(
                        functionId = "63b52202d0f19ffc2478",
                        data = mapOf("name" to "param + ${UUID.randomUUID()}").toJson(),
                        async = false
                    )
                    println(functions)
                    println("Trigger result: ${response.toJson()}")
                } catch (e: AppwriteException) {
                    print("${e.message}, ${e.code}, ${e.response}")
                }
            }
        } catch (e: AppwriteException) {
            print("AppwriteException: ${e.message}, ${e.code}, ${e.response}")
            e.printStackTrace()
        } catch (e: Exception) {
            print(e.message)
        }


        setContent {
            AppWriteTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppWriteTestTheme {
        Greeting("Android")
    }
}