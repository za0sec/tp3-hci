package com.example.app_grupo13.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.app_grupo13.ui.screens.CardsScreen
import com.example.app_grupo13.ui.screens.WelcomeScreen
import com.example.app_grupo13.ui.screens.RegisterScreen
import com.example.app_grupo13.ui.screens.LoginScreen
import com.example.app_grupo13.ui.screens.DashboardScreen
import com.example.app_grupo13.ui.screens.MovementsScreen
import com.example.app_grupo13.ui.screens.ProfileScreen
import com.example.app_grupo13.ui.screens.ResetNewPasswordScreen
import com.example.app_grupo13.ui.screens.ResetPasswordCodeScreen
import com.example.app_grupo13.ui.screens.ResetPasswordScreen
import com.example.app_grupo13.ui.screens.VerifyScreen
import com.example.app_grupo13.ui.theme.PlumTheme
import com.example.app_grupo13.ui.screens.DepositScreen
import com.example.app_grupo13.ui.screens.TransferScreen
import com.example.app_grupo13.ui.screens.PayScreen
import com.example.app_grupo13.ui.viewmodels.UserViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.app_grupo13.data.network.RemoteDataSource
import com.example.app_grupo13.data.repository.UserRepository
import com.example.app_grupo13.ui.viewmodels.UserViewModelFactory
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_grupo13.ui.screens.Dashboard
import com.example.app_grupo13.ui.viewmodels.DashboardViewModelFactory
import com.example.app_grupo13.ui.viewmodels.LoginViewModelFactory
import com.example.app_grupo13.ui.viewmodels.VerifyViewModelFactory
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import com.example.app_grupo13.databinding.ActivityMainBinding
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit


class MainActivity : ComponentActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        cameraExecutor = Executors.newSingleThreadExecutor()

        val remoteDataSource = RemoteDataSource(this)
        val userRepository = UserRepository(remoteDataSource)
        val userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(this)
        )[UserViewModel::class.java]

        setContent {
            PlumTheme {
                AppNavigation(userViewModel)
            }
        }
    }

    private fun takePhoto() {}

    private fun captureVideo() {}

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = viewBinding.viewFinder.surfaceProvider
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

}



@Composable
fun AppNavigation(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("register") { RegisterScreen(navController, userViewModel) }
        composable("login") { 
            LoginScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = LoginViewModelFactory(LocalContext.current)
                )
            ) 
        }
        composable("reset_password") { ResetPasswordScreen(navController) }
        composable("dashboard") { 
            Dashboard(
                navController = navController,
                viewModel = viewModel(
                    factory = DashboardViewModelFactory(LocalContext.current)
                )
            ) 
        }
        composable("movements") { MovementsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable(
            route = "verify/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            VerifyScreen(
                navController = navController,
                email = backStackEntry.arguments?.getString("email"),
                viewModel = viewModel(
                    factory = VerifyViewModelFactory(LocalContext.current)
                )
            )
        }
        composable("reset_password") { ResetPasswordScreen(navController) }
        composable("reset_password_code"){ResetPasswordCodeScreen(navController)}
        composable("reset_password_new") { ResetNewPasswordScreen(navController) }
        composable("deposit") { DepositScreen(navController) }
        composable("transfer") { TransferScreen(navController) }
        composable("pay") { PayScreen(navController) }
        composable("cards"){CardsScreen(navController)}
    }
}
