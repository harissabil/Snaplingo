# Snaplingo
[![)](https://img.shields.io/badge/latest-v1.0.0--alpha.1-orange)](https://github.com/harissabil/Snaplingo/releases)

Snaplingo adalah aplikasi edukasi di mobile yang mengubah cara kita mempelajari bahasa Inggris dengan menyatukan pendekatan inovatif dan gamifikasi yang menarik pengguna dapat mengeksplorasi dunia sekitar mereka sambil belajar bahasa Inggris dengan mudah.

## Fitur
<table>
  <tbody>
    <tr>
      <td align="center" width="25%">
        Snap Object
      </td>
      <td align="center" width="25%">
        Flashcard
      </td>
      <td align="center" width="25%">
        Task
      </td>
      <td align="center" width="25%">
        Interactive Map
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="assets/screenshot/snap_object.jpg?raw=true" width="100%" class="responsive-img"/>
      </td>
      <td align="center">
        <img src="assets/screenshot/flashcard.jpg?raw=true" width="100%" class="responsive-img"/>
      </td>
      <td align="center">
        <img src="assets/screenshot/task.jpg?raw=true" width="100%" class="responsive-img"/>
      </td>
      <td align="center">
        <img src="assets/screenshot/interactive_map.jpg?raw=true" width="100%" class="responsive-img"/>
      </td>
    </tr>
  </tbody>
</table>

- Snap Object:
  - User memilih tombol "Snap Object" dari halaman utama.
  - Mereka menggunakan kamera ponsel mereka untuk mengambil foto objek di sekitar mereka.
  - Setelah mengambil foto, aplikasi memproses gambar tersebut dan memberikan informasi tentang objek dalam bahasa Inggris dan bahasa Indonesia.

- Flashcard:
  - User memilih menu "Flashcard" dari halaman utama.
  - Mereka diperlihatkan sejumlah kata-kata atau gambar yang telah mereka dapatkan sebelumnya.
  - Pengguna dapat memilih untuk menerjemahkan kata-kata tersebut, atau mereka dapat mencoba mengingatnya sendiri.

- Task:
  - User memilih menu "Task" dari halaman utama.
  - Mereka diberikan tantangan atau misi tertentu yang harus diselesaikan.
  - Setelah menyelesaikan misi, pengguna mendapatkan exp atau reward tertentu.

- Interactive Map:
  - User bisa melihat map dan legenda dari lokasi sekitarnya di halaman utama.
  - Mereka bisa melihat lokasi objek yang telah diambil sebelumnya.
  - User bisa melacak lokasinya sendiri dan melihatnya di maps.
 
## Instalasi

Untuk meng-install Snaplingo, ada dua opsi yang tersedia:

### Opsi 1: Install dari GitHub Release

| varian | tanggal | versi terbaru | link |
|----------|-------------|--------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| v1-alpha | 2024-05-09 | [v1.0.0-alpha.1](https://github.com/harissabil/Snaplingo/releases/tag/v1.0.0-alpha.1) | [APK](https://github.com/harissabil/Snaplingo/releases/download/v1.0.0-alpha.1/app-release.apk) |

> _Lihat bagian [Releases](https://github.com/harissabil/Snaplingo/releases) untuk lebih banyak lagi._

### Opsi 2: Build dan jalankan dari source code

1. Clone atau download proyek dan buka di Android Studio.
2. Buat file `local.properties` di dalam folder root proyek jika belum ada.
3. Tambahkan `MAPS_API_KEY` dan `HUGGING_FACE_API_KEY` di dalam file seperti yang ditunjukkan di bawah ini.

```android
...

MAPS_API_KEY="isi dengan api key google maps"
HUGGING_FACE_API_KEY="isi dengan api key hugging face"
```

5. Sync the project with Gradle and run the app on an Android emulator or a physical Android device.
6. Sinkronisasi proyek dengan Gradle dan jalankan aplikasi melalui Emulator atau Device langsung.
