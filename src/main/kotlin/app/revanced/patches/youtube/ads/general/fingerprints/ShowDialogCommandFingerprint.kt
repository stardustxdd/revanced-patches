package app.revanced.patches.youtube.ads.general.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import app.revanced.patches.youtube.utils.resourceid.SharedResourceIdPatch.SlidingDialogAnimation
import app.revanced.util.containsWideLiteralInstructionIndex
import com.android.tools.smali.dexlib2.Opcode

internal object ShowDialogCommandFingerprint : MethodFingerprint(
    returnType = "V",
    opcodes = listOf(
        Opcode.IF_EQ,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.IGET, // get dialog code
    ),
    // 18.43 and earlier has a different first parameter.
    // Since this fingerprint is somewhat weak, work around by checking for both method parameter signatures.
    customFingerprint = custom@{ methodDef, _ ->
        if (!methodDef.containsWideLiteralInstructionIndex(SlidingDialogAnimation)) {
            return@custom false
        }
        // 18.43 and earlier parameters are: "L", "L"
        // 18.44+ parameters are "[B", "L"
        val parameterTypes = methodDef.parameterTypes

        parameterTypes.size == 2 && parameterTypes[1].startsWith("L")
    },
)