package com.singhastudios.smartrail

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.singhastudios.smartrail.ui.theme.SmartRailTheme
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRailTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("main") { MainContent() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("SmartRail") })
        }
    ) { innerPadding ->
        SmartRailUI(modifier = Modifier.padding(innerPadding)) // ✅ Pass padding
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Train Icon
            Image(
                painter = painterResource(id = R.drawable.train_main),
                contentDescription = "Train Icon",
                modifier = Modifier.size(64.dp),
            )

            // App Title
            Text(
                text = "SmartRail UI",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Tagline
            Text(
                text = "Connecting Journeys, Simplifying Travel",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // "Get Started" Button at the bottom center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Get Started", color = Color.White)
            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.train_main),
            contentDescription = "SmartRail Logo",
            modifier = Modifier.size(48.dp),
        )

        Text(
            text = "SmartRail UI",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E88E5),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("main") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text("Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { /* Handle Register */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forgot Password?",
            color = Color(0xFF1E88E5),
            fontSize = 14.sp,
            modifier = Modifier.clickable { /* Handle Forgot Password */ }
        )
    }
}




@Composable
fun SmartRailUI(modifier: Modifier) {
    var selectedStation by remember { mutableStateOf("Nattandiya Station") }
    var trainLocation by remember { mutableStateOf(LatLng(7.4000, 79.8400)) }
    var stationLocation by remember { mutableStateOf(LatLng(7.4500, 79.8500)) }
    var distance by remember { mutableDoubleStateOf(0.0) }
    var trainStatus by remember { mutableStateOf("Running") }
    var expectedArrival by remember { mutableStateOf("21:49:00") }
    var gateControl by remember { mutableStateOf(false) }

    // Listen for Firebase updates
    val database = FirebaseDatabase.getInstance("https://smartrail-f3b9b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val trainRef = database.getReference("trains/049/location")

    LaunchedEffect(Unit) {
        trainRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = snapshot.child("lat").getValue(Double::class.java) ?: 0.0
                val lng = snapshot.child("lng").getValue(Double::class.java) ?: 0.0
                trainLocation = LatLng(lat, lng)

                println("Distance: $trainLocation km")


                // Update distance when train location changes
                distance = calculateDistance(trainLocation, stationLocation)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching train location: ${error.message}")
            }
        })
    }

    // Function to calculate distance between two LatLng points
    fun calculateDistance(train: LatLng, station: LatLng): Double {
        return SphericalUtil.computeDistanceBetween(train, station) / 1000 // Convert meters to KM
    }

// Display distance in UI
    Text(text = "Distance to station: ${"%.2f".format(distance)} km")

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dropdown for station selection
        DropdownMenuExample(selectedStation, onStationSelected = { selectedStation = it })

        Text(text = selectedStation, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        // Google Map View
        MapView(trainLocation, stationLocation)

        // Distance Info
        DistanceCard(distance)

        // Train Info Card
        TrainInfoCard(trainStatus, expectedArrival)

        // Gate Control
        GateControlSwitch(gateControl) { gateControl = it }
    }

}

@Composable
fun MapView(trainLocation: LatLng, stationLocation: LatLng) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(stationLocation, 12f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(state = MarkerState(position = stationLocation), title = "Station")
        Marker(state = MarkerState(position = trainLocation), title = "Train")
    }
}

fun calculateDistance(start: LatLng, end: LatLng): Double {
    val r = 6371 // Radius of Earth in km
    val dLat = Math.toRadians(end.latitude - start.latitude)
    val dLon = Math.toRadians(end.longitude - start.longitude)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(start.latitude)) * cos(Math.toRadians(end.latitude)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c
}

@Composable
fun DropdownMenuExample(selectedStation: String, onStationSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val stations = listOf("Nattandiya Station", "Colombo Fort", "Kandy")

    Box {
        Button(onClick = { expanded = true }) { Text(selectedStation) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            stations.forEach { station ->
                DropdownMenuItem(
                    text = { Text("Select Station") },  // ✅ Correct way to set text
                    onClick = {
                    onStationSelected(station)
                    expanded = false
                })
            }
        }
    }
}


@Composable
fun DistanceCard(distance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Distance to the station:", fontSize = 16.sp)
            Text(text = "${String.format("%.1f", distance)} km", fontSize = 24.sp, color = Color.Blue)
        }
    }
}


@Composable
fun TrainInfoCard(status: String, expectedArrival: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Train Number: 049", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Status: $status", fontSize = 18.sp)
            Text("Expected Arrival: $expectedArrival", fontSize = 18.sp)
        }
    }
}

@Composable
fun GateControlSwitch(gateControl: Boolean, onGateControlChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Gate Control", fontSize = 18.sp)
        Switch(checked = gateControl, onCheckedChange = onGateControlChange)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(navController = rememberNavController())
}