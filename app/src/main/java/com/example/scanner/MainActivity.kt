package com.example.scanner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.scanner.ui.theme.ScannerTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScannerTheme {
                ScannerScreen()
            }
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Manejar cambios de configuración (aun no es necesario)
    }
}

@Composable
fun ScannerScreen(viewModel: ScannerViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var resultadoEscaneo by remember { mutableStateOf("") }
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            result.contents?.let { barcode ->
                viewModel.getProductInfo(barcode)
            }
        }
    )

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val options = ScanOptions()
                options.setOrientationLocked(false)
                options.setPrompt("Escanea el código de barras")
                options.setBeepEnabled(true)
                options.setBarcodeImageEnabled(true)
                scanLauncher.launch(options)
            }
        ) {
            Text(text = "Escanear")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is ScannerViewModel.UiState.Initial -> Text("Escanea un producto para ver su información")
            is ScannerViewModel.UiState.Loading -> CircularProgressIndicator()
            is ScannerViewModel.UiState.Success -> ProductInfo((uiState as ScannerViewModel.UiState.Success).product)
            is ScannerViewModel.UiState.Error -> Text((uiState as ScannerViewModel.UiState.Error).message, color = Color.Red)
        }
    }
}

@Composable
fun ProductInfo(product: Product) {
    Column {
        Text("Nombre: ${product.product_name ?: "N/A"}")
        Text("Marca: ${product.brands ?: "N/A"}")
        Text("Categoría: ${product.categories ?: "N/A"}")
        product.nutriments?.let { nutriments ->
            Text("Calorías: ${nutriments.energy_100g ?: "N/A"} kcal/100g")
            Text("Grasas: ${nutriments.fat_100g ?: "N/A"} g/100g")
            Text("Carbohidratos: ${nutriments.carbohydrates_100g ?: "N/A"} g/100g")
            Text("Proteínas: ${nutriments.proteins_100g ?: "N/A"} g/100g")
        }
    }
}
