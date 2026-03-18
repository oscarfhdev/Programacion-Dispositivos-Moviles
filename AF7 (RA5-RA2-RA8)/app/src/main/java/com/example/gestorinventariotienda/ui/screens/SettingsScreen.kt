package com.example.gestorinventariotienda.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestorinventariotienda.ui.viewmodels.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val storeName by viewModel.storeName.collectAsState()
    val storeSsid by viewModel.storeWifiSsid.collectAsState()
    val incidents by viewModel.incidentsFlow.collectAsState()

    // Estado local del campo de nombre: solo se escribe en DataStore al salir del campo
    var localStoreName by remember { mutableStateOf(storeName) }
    var localStoreSsid by remember { mutableStateOf(storeSsid) }

    // Sincroniza el estado local la primera vez que llegan los datos de DataStore
    LaunchedEffect(storeName) { if (localStoreName != storeName) localStoreName = storeName }
    LaunchedEffect(storeSsid) { if (localStoreSsid != storeSsid) localStoreSsid = storeSsid }

    var showIncidentDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Ajustes y Buzón", fontWeight = FontWeight.Bold) }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Configuración",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Estado local que solo persiste al salir del campo (onFocusChanged)
                    OutlinedTextField(
                        value = localStoreName,
                        onValueChange = { localStoreName = it }, // Solo actualiza estado local
                        label = { Text("Nombre de la tienda") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focus ->
                                // Guardamos en DataStore únicamente al perder el foco
                                if (!focus.isFocused) viewModel.updateStoreName(localStoreName)
                            },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.updateStoreName(localStoreName)
                            focusManager.clearFocus()
                        })
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = localStoreSsid,
                        onValueChange = { localStoreSsid = it }, // Solo actualiza estado local
                        label = { Text("SSID Wi-Fi Requerida para Descargas") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focus ->
                                // Guardamos en DataStore únicamente al perder el foco
                                if (!focus.isFocused) viewModel.updateWifiSsid(localStoreSsid)
                            },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.updateWifiSsid(localStoreSsid)
                            focusManager.clearFocus()
                        })
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showIncidentDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Redactar Reporte de Incidencia", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Historial de Incidencias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (incidents.isEmpty()) {
                Text(
                    "No hay incidencias registradas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(incidents, key = { it.id }) { incident ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    incident.description,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                    .format(Date(incident.timestamp))
                                Text(date, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showIncidentDialog) {
        var incidentText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showIncidentDialog = false },
            title = { Text("Nueva Incidencia", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = incidentText,
                    onValueChange = { incidentText = it },
                    label = { Text("Describe el problema") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (incidentText.isNotBlank()) {
                            viewModel.addIncident(incidentText)
                            showIncidentDialog = false
                        }
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showIncidentDialog = false }) { Text("Cancelar") }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
