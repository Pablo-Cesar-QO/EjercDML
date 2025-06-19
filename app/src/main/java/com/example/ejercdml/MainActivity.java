package com.example.ejercdml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_NAME = "personas_data.txt";
    private EditText etNombre, etEdad;
    private RadioGroup rgEstadoCivil;
    private Spinner spGenero;
    private CheckBox cbTrabaja, cbEstudiante;
    private Switch swNotificaciones;
    private ToggleButton tbVip;
    private SeekBar sbSatisfaccion;
    private TextView tvSatisfaccion, tvFileStreamTag;
    private DatePicker dpFechaRegistro;
    private TimePicker tpHoraRegistro;
    private Button btnInsertar, btnMostrarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.etNombre);
        etEdad = findViewById(R.id.etEdad);
        rgEstadoCivil = findViewById(R.id.rgEstadoCivil);
        spGenero = findViewById(R.id.spGenero);
        cbTrabaja = findViewById(R.id.cbTrabaja);
        cbEstudiante = findViewById(R.id.cbEstudiante);
        swNotificaciones = findViewById(R.id.swNotificaciones);
        tbVip = findViewById(R.id.tbVip);
        sbSatisfaccion = findViewById(R.id.sbSatisfaccion);
        tvSatisfaccion = findViewById(R.id.tvSatisfaccion);
        tvFileStreamTag = findViewById(R.id.tvFileStreamTag);
        dpFechaRegistro = findViewById(R.id.dpFechaRegistro);
        tpHoraRegistro = findViewById(R.id.tpHoraRegistro);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnMostrarDatos = findViewById(R.id.btnMostrarDatos);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.generos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapter);

        sbSatisfaccion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSatisfaccion.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnInsertar.setOnClickListener(v -> insertarDatos());
        btnMostrarDatos.setOnClickListener(v -> {
            if (hasSavedData()) {
                Intent intent = new Intent(MainActivity.this, MostrarDatosActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "No hay datos guardados aún", Toast.LENGTH_SHORT).show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        dpFechaRegistro.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        tpHoraRegistro.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        tpHoraRegistro.setMinute(calendar.get(Calendar.MINUTE));

        tvFileStreamTag.setText("Almacenamiento: FileStream");
    }

    private void insertarDatos() {
        try {
            String nombre = etNombre.getText().toString().trim();
            String edad = etEdad.getText().toString().trim();

            if (nombre.isEmpty() || edad.isEmpty()) {
                Toast.makeText(this, "Nombre y edad son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int estadoCivilId = rgEstadoCivil.getCheckedRadioButtonId();
            RadioButton rbEstadoCivil = findViewById(estadoCivilId);
            String estadoCivil = rbEstadoCivil.getText().toString();

            String genero = spGenero.getSelectedItem().toString();

            boolean trabaja = cbTrabaja.isChecked();
            boolean estudiante = cbEstudiante.isChecked();

            boolean notificaciones = swNotificaciones.isChecked();

            boolean vip = tbVip.isChecked();

            int satisfaccion = sbSatisfaccion.getProgress();

            int day = dpFechaRegistro.getDayOfMonth();
            int month = dpFechaRegistro.getMonth() + 1;
            int year = dpFechaRegistro.getYear();
            String fechaRegistro = String.format("%04d-%02d-%02d", year, month, day);

            int hour = tpHoraRegistro.getHour();
            int minute = tpHoraRegistro.getMinute();
            String horaRegistro = String.format("%02d:%02d", hour, minute);

            String registro = String.format(
                    "=== Registro ===\n" +
                            "Nombre: %s\nEdad: %s\nEstado civil: %s\nGénero: %s\n" +
                            "Trabaja: %s\nEstudiante: %s\nNotificaciones: %s\nVIP: %s\n" +
                            "Satisfacción: %d/10\nFecha registro: %s\nHora registro: %s\n\n",
                    nombre, edad, estadoCivil, genero,
                    trabaja ? "Sí" : "No", estudiante ? "Sí" : "No",
                    notificaciones ? "Activadas" : "Desactivadas", vip ? "Sí" : "No",
                    satisfaccion, fechaRegistro, horaRegistro);

            guardarEnArchivo(registro);

            Toast.makeText(this, "Datos guardados con FileStream", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarEnArchivo(String datos) {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(datos);
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasSavedData() {
        File file = new File(getFilesDir(), FILE_NAME);
        return file.exists() && file.length() > 0;
    }
//SOLO VOY A COMENTAR PARA EL EJERCICIO :)
    private void limpiarCampos() {
        etNombre.setText("");
        etEdad.setText("");
        ((RadioButton) findViewById(R.id.rbCasado)).setChecked(true);
        spGenero.setSelection(0);
        cbTrabaja.setChecked(false);
        cbEstudiante.setChecked(false);
        swNotificaciones.setChecked(false);
        tbVip.setChecked(false);
        sbSatisfaccion.setProgress(5);

        Calendar calendar = Calendar.getInstance();
        dpFechaRegistro.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        tpHoraRegistro.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        tpHoraRegistro.setMinute(calendar.get(Calendar.MINUTE));
    }

    public static String leerDatosGuardados(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}