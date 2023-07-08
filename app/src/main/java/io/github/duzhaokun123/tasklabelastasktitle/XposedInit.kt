package io.github.duzhaokun123.tasklabelastasktitle

import android.app.ActivityManager.TaskDescription
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.github.kyuubiran.ezxhelper.utils.paramCount
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        EzXHelperInit.initHandleLoadPackage(lpparam)
        loadClass("com.android.quickstep.TaskUtils")
            .findMethod { name == "getTitle" && paramCount == 2 }
            .hookBefore { param ->
                val task = param.args[1]
                val taskDescription = task.getObjectAs<TaskDescription>("taskDescription")
                taskDescription.label.takeUnless { it.isNullOrBlank() }?.let {
                    param.result = it
                }
            }
    }
}