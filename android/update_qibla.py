import os

# New QiblaContent implementation with dynamic location
qibla_content = """@Composable
fun QiblaContent() {
    val context = LocalContext.current
    var azimuth by remember { mutableFloatStateOf(0f) }
    
    // Low pass filter variables to smooth out sensor jitter
    val alpha = 0.05f
    var smoothedAzimuth by remember { mutableFloatStateOf(0f) }

    // Kaaba coordinates
    val kaabaLat = Math.toRadians(21.4225)
    val kaabaLon = Math.toRadians(39.8262)

    // Dynamic user location
    var userLat by remember { mutableDoubleStateOf(Math.toRadians(17.385)) } // Default to Hyderabad
    var userLon by remember { mutableDoubleStateOf(Math.toRadians(78.4867)) }
    var locationFetched by remember { mutableStateOf(false) }

    // Request Location Permission
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLat = Math.toRadians(location.latitude)
                        userLon = Math.toRadians(location.longitude)
                        locationFetched = true
                    }
                }
            } catch (e: SecurityException) {
                // Ignore, permission was checked
            }
        }
    }

    LaunchedEffect(Unit) {
        val permissionStatus = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLat = Math.toRadians(location.latitude)
                        userLon = Math.toRadians(location.longitude)
                        locationFetched = true
                    }
                }
            } catch (e: SecurityException) { }
        } else {
            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Calculate Qibla direction based on (dynamic) user location
    val qiblaBearing = remember(userLat, userLon) {
        val dLon = kaabaLon - userLon
        val y = kotlin.math.sin(dLon) * kotlin.math.cos(kaabaLat)
        val x = kotlin.math.cos(userLat) * kotlin.math.sin(kaabaLat) - kotlin.math.sin(userLat) * kotlin.math.cos(kaabaLat) * kotlin.math.cos(dLon)
        val bearing = Math.toDegrees(atan2(y, x)).toFloat()
        (bearing + 360) % 360
    }

    // Sensor-based compass
    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        var gravity: FloatArray? = null
        var geomagnetic: FloatArray? = null

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
                }
                val g = gravity ?: return
                val m = geomagnetic ?: return

                val r = FloatArray(9)
                val i = FloatArray(9)
                if (SensorManager.getRotationMatrix(r, i, g, m)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(r, orientation)
                    val currentAzimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    
                    // Apply low-pass filter for smooth needle movement
                    // Handle wrap-around at 360 degrees
                    var diff = currentAzimuth - smoothedAzimuth
                    if (diff < -180f) diff += 360f
                    if (diff > 180f) diff -= 360f
                        
                    smoothedAzimuth += alpha * diff
                    // Normalize to 0-360
                    if (smoothedAzimuth < 0) smoothedAzimuth += 360f
                    if (smoothedAzimuth >= 360f) smoothedAzimuth -= 360f
                    
                    azimuth = smoothedAzimuth
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        accelerometerSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometerSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    val needleRotation = qiblaBearing - azimuth
    val animatedRotation by animateFloatAsState(
        targetValue = needleRotation,
        animationSpec = tween(durationMillis = 300),
        label = "compass_rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Qibla Direction",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = DarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (locationFetched) "Using Live GPS accurate direction" else "Point the arrow toward the Qibla",
            style = MaterialTheme.typography.bodyMedium,
            color = if (locationFetched) PrimaryGreen else GrayText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Compass circle - Premium styling
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(LightGreen, CardWhite),
                        radius = 400f
                    ), 
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Inner decorative ring
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = PrimaryGreen.copy(alpha = 0.2f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }
            }
            // Outer ring markers
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Color.Transparent)
            ) {
                // N marker - North indicator style
                Text("N", modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = PrimaryGreen)
                // S marker
                Text("S", modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = GrayText)
                // E marker
                Text("E", modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = GrayText)
                // W marker
                Text("W", modifier = Modifier.align(Alignment.CenterStart).padding(start = 10.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = GrayText)
            }

            // Rotating compass rose and needle to point to Qibla
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .rotate(animatedRotation)
                    .align(Alignment.Center)
            ) {
                // Outer circle of the pointer
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = PrimaryGreen,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
                    )
                    drawCircle(
                        color = PrimaryGreen.copy(alpha = 0.1f)
                    )
                }
                
                // Kaaba Icon pointer
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        Icons.Filled.Navigation,
                        contentDescription = "Qibla Direction",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bearing info card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Qibla Bearing",
                    style = MaterialTheme.typography.labelLarge,
                    color = GrayText
                )
                Text(
                    text = "${qiblaBearing.toInt()}°",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🕋 Makkah Al-Mukarramah",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )
            }
        }
    }
}"""

with open('app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt', 'r', encoding='utf-8') as f:
    content = f.read()

start_idx = content.find("@Composable\nfun QiblaContent()")
end_idx = content.find("@Composable\nfun ToolTabItem(")

if start_idx != -1 and end_idx != -1:
    new_content = content[:start_idx] + qibla_content + "\n\n" + content[end_idx:]
    with open('app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt', 'w', encoding='utf-8') as f:
        f.write(new_content)
    print("QiblaContent replaced successfully.")
else:
    print("Could not locate QiblaContent block limits.")
