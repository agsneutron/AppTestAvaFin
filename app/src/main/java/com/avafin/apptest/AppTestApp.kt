package com.avafin.apptest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// usamos la notación HiltAndroidApp de dagger hilt para que AppTestApp genere  el código
// de la clase AppTestApp en tiempo de compilación
@HiltAndroidApp
class AppTestApp: Application()