import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

var sourceBase = ""
var targetBase = ""
var inputFullStr = ""
var inputIntegerStr = ""
var inputFractionStr = ""
var integerConversionResult = ""
var fractionConversionResult = ""
var hasFraction = false

fun main() {

    // get the input for source and target base
    getSourceAndTargetBaseInput()

    while (sourceBase != "/exit") {
        // get input number
        getNumInput()
        while (inputIntegerStr != "/back") {
            if (sourceBase == "10") {
                integerConversionResult =
                    convertIntegerFromDecimal(inputIntegerStr.toBigInteger(), targetBase.toBigInteger())
                if (hasFraction) {
                    fractionConversionResult =
                        convertFractionFromDecimal(inputFractionStr, targetBase.toBigDecimal())
                }
            } else if (targetBase == "10") {
                integerConversionResult = convertIntegerToDecimal(inputIntegerStr, sourceBase.toBigInteger())
                if (hasFraction) {
                    fractionConversionResult =
                        convertFractionToDecimal(inputFractionStr, sourceBase.toBigDecimal())
                }
            }
            // if the target or source base is not 10, we will use output of convertToDecimal
            // as input of convertFromDecimal to convert between non-decimal bases
            else {
                val tempNumberInDecimal = convertIntegerToDecimal(inputIntegerStr, sourceBase.toBigInteger())
                integerConversionResult =
                    convertIntegerFromDecimal(tempNumberInDecimal.toBigInteger(), targetBase.toBigInteger())
                val tempNumberInFraction = convertFractionToDecimal(inputFractionStr, sourceBase.toBigDecimal())
                fractionConversionResult =
                    convertFractionFromDecimal(tempNumberInFraction, targetBase.toBigDecimal())

            }
            // print the output
            output()
            // get number input again after converting
            getNumInput()
        }
        // get bases input again when user enters /back
        getSourceAndTargetBaseInput()
    }
}

fun getSourceAndTargetBaseInput() {
    println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
    val input = readLine()!!.split(" ")
    sourceBase = input[0]
    if (input.lastIndex == 1) {
        targetBase = input[1]
    }
}

fun getNumInput() {
    println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
    inputFullStr = readLine()!!.toString()
    if (inputFullStr.contains(".")) {
        inputIntegerStr = inputFullStr.substringBefore(".")
        inputFractionStr = inputFullStr.substringAfter(".")
        hasFraction = true
    } else {
        inputIntegerStr = inputFullStr
        hasFraction = false
    }
}

fun convertIntegerToDecimal(sourceInteger: String, sourceBase: BigInteger): String {
    val str = sourceInteger.reversed()
    var numInDecimal = BigInteger.valueOf(0)
    var count = 0
    var multiplier = BigInteger.valueOf(1)

    // get each character in the reversed string and convert it to decimal
    for (i in str) {
        var c = i.toString()
        var numIncrement = 10
        repeat(count) {
            multiplier *= sourceBase
        }
        // if sourceBase is bigger than 10, convert the input alphabets to numbers
        if (sourceBase >= BigInteger.valueOf(11)) {
            for (j in 'A'..'Z') {
                if (j.toString() == c.uppercase()) {
                    c = numIncrement.toString()
                    break
                }
                numIncrement++
            }
        }
        numInDecimal += c.toBigInteger() * multiplier
        multiplier = BigInteger.valueOf(1)
        count++
    }
    return numInDecimal.toString()
}

fun convertFractionToDecimal(sourceFraction: String, sourceBase: BigDecimal): String {

    var numInDecimal = BigDecimal.valueOf(0)
    var count = 1
    // set scale to multiplier to avoid rounding the result to zero
    var multiplier = BigDecimal.valueOf(1).setScale(10, RoundingMode.HALF_DOWN)

    // iterate though fraction and convert it to decimal
    for (i in sourceFraction) {
        var c = i.toString()
        var numIncrement = 10
        repeat(count) {
            multiplier /= sourceBase
        }
        // if sourceBase is bigger than 10, convert the input alphabets to numbers
        if (sourceBase >= BigDecimal.valueOf(11)) {
            for (j in 'A'..'Z') {
                if (j.toString() == c.uppercase()) {
                    c = numIncrement.toString()
                    break
                }
                numIncrement++
            }
        }
        numInDecimal += c.toBigDecimal() * multiplier
        multiplier = BigDecimal.valueOf(1).setScale(10, RoundingMode.HALF_DOWN)
        count++
    }
    numInDecimal = numInDecimal.setScale(5, RoundingMode.HALF_DOWN)
    return numInDecimal.toString().substringAfter(".")
}

fun convertIntegerFromDecimal(integerInDecimal: BigInteger, targetBase: BigInteger): String {
    var quotient = integerInDecimal
    var remainder: BigInteger
    var str = ""

    if (quotient == BigInteger("0")) {
        str = "0"
    }

    while (quotient != BigInteger.valueOf(0)) {
        remainder = quotient % targetBase
        quotient /= targetBase
        var alphaIncrement = 'A'

        // if targetBase is greater than 10, convert the number to corresponding alphabet
        if (targetBase >= BigInteger.valueOf(11)) {
            for (j in 10..36) {
                if (BigInteger.valueOf(j.toLong()) == remainder) {
                    str += alphaIncrement.uppercase()
                    break
                }
                alphaIncrement++
            }
        }

        if (remainder < BigInteger.valueOf(10)) {
            str += "$remainder"
        }
    }
    return str.reversed()
}

fun convertFractionFromDecimal(fractionInDecimal: String, targetBase: BigDecimal): String {
    val fractionalValue: BigDecimal = "0.$fractionInDecimal".toBigDecimal()
    var fractionRemainder = fractionalValue
    var integerRemainder: BigDecimal
    var fractionString = ""
    var tempString: String

    while (fractionString.length < 5) {
        integerRemainder = fractionRemainder * targetBase
        fractionRemainder =
            "0.${integerRemainder.toString().substringAfter(".")}"
                .toBigDecimal()
        fractionRemainder = fractionRemainder.setScale(5, RoundingMode.HALF_DOWN)
        tempString = integerRemainder.toString().substringBefore(".")

        var alphaIncrement = 'A'

        // if targetBase is greater than 10, convert the number to corresponding alphabet
        if (targetBase >= BigDecimal.valueOf(11)) {
            for (j in 10..36) {
                if (j.toString() == tempString) {
                    fractionString += alphaIncrement.uppercase()
                    break
                }
                alphaIncrement++
            }
        }

        if (tempString.toInt() < 10) {
            fractionString += tempString
        }
    }
    return fractionString
}

fun output() {
    if (hasFraction) {
        println("Conversion result: $integerConversionResult.$fractionConversionResult")
    } else {
        println("Conversion result: $integerConversionResult")
    }
}