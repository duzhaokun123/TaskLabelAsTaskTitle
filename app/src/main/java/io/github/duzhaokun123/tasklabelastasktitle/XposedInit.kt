package io.github.duzhaokun123.tasklabelastasktitle

import android.app.Application
import android.app.IActivityTaskManager
import android.os.ServiceManager
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.invokeMethod
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.github.kyuubiran.ezxhelper.utils.paramCount
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage {
    lateinit var iActivityTaskManager: IActivityTaskManager
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        EzXHelperInit.initHandleLoadPackage(lpparam)
        loadClass("com.android.quickstep.TaskUtils")
            .findMethod { name == "getTitle" && paramCount == 2 }
            .hookBefore { param ->
                val task = param.args[1]
                val taskDescription = iActivityTaskManager.getTaskDescription(
                    task.getObject("key").getObjectAs("id")
                )
                taskDescription.label?.takeUnless { it.isEmpty() }?.let {
                    param.result = it
                }
            }
        Application::class.java
            .findMethod { name == "onCreate" }
            .hookAfter {
                iActivityTaskManager =
                    IActivityTaskManager.Stub.asInterface(ServiceManager.getService("activity_task"))
            }
    }
}