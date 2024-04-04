package com.example.tokoxyz2024android

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.media.audiofx.EnvironmentalReverb
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.tokoxyz2024android.R
import com.example.tokoxyz2024android.adapter.InvoiceAdapter
import com.example.tokoxyz2024android.data.model.ApiResponse
import com.example.tokoxyz2024android.databinding.ActivityInvoiceBinding
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.NumberFormat
import java.util.Locale

class InvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInvoiceBinding.inflate(layoutInflater)
        val transaksi = Gson().fromJson(intent.getStringExtra("transaksi"), ApiResponse::class.java)

        Log.v("TransaksiData", transaksi.toString())
        binding.itemList.adapter = InvoiceAdapter(transaksi.data as List<Map<String, Any>>)

        var totalHarga = 0.0
        var tanggalTransaksi = ""

        for(data in transaksi.data){
            tanggalTransaksi = data["tgl_transaksi"].toString() + "_" + data["no_transaksi"].toString()
            totalHarga += data["harga"].toString().toDouble() * data["qty"].toString().toDouble()
        }

        val formater = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        binding.bayar.text = formater.format(totalHarga).replace("Rp", "Rp. ")

        binding.save.setOnClickListener{
            generatePDF(binding.root, tanggalTransaksi)
        }

        binding.share.setOnClickListener{
            val pdfFile = generatePDF(binding.root, tanggalTransaksi)
            if(pdfFile != null){
                sharePDF(pdfFile)
            }
        }

        binding.selesai.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        setContentView(binding.root)
    }

    private fun generatePDF(view: View, date: String): File?{
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
        val page = document.startPage(pageInfo)

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val bitmapDrawable = BitmapDrawable(resources, bitmap)
        bitmapDrawable.setBounds(0, 0, view.width, view.height)
        bitmapDrawable.draw(page.canvas)

        document.finishPage(page)

        val fileName = "invoice_$date.pdf"
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(
            path,
            fileName
        )

        try{
            val outputStream: OutputStream = FileOutputStream(file)
            document.writeTo(outputStream)
            outputStream.flush()
            outputStream.close()
            Toast.makeText(this, "File disimpan di: $path", Toast.LENGTH_SHORT).show()
            return file
        } catch (e: Exception){
            e.printStackTrace()
            return null
        }
        document.close()
    }

    private fun sharePDF(file: File){
        val uri = Uri.fromFile(file)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        startActivity(Intent.createChooser(intent, "Bagikan Dengan"))
    }
}