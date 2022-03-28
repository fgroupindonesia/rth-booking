https://api.whatsapp.com/send/?phone=6285871341474&text=message

  var url_string = window.location.href;
var url = new URL(url_string);
var nama = url.searchParams.get("nama");
var tanggal = url.searchParams.get("tanggal");
var jam = url.searchParams.get("jam");
 
   Cognito.load("forms", { id: "3", entry: { "NamaLengkap": nama } });

https://api.whatsapp.com/send/?phone=6285871341474&text=saya+*NamaLengkap*+sudah+mengisi+*Progress+Notes*+\n+barusan+mohon+diperiksa+datanya+oleh+*Admin*+terima+kasih  

https://api.whatsapp.com/send/?phone=6285871341474&text=saya+*NamaLengkap*+sudah+mengisi+*Formulir+Pendaftaran*+\n+barusan+mohon+dijadwalkan+*therapy+JadwalTherapy.SayaInginTindakanTreatment*+dengan+pilihan+jadwal+*JadwalTherapy.Tanggal*+di+jam+*JadwalTherapy.Jam﻿*+mohon+ditindak+lanjuti+%0a%0a+terima+kasih