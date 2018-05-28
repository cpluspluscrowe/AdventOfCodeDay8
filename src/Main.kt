package main
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue

fun main(args: Array<String>) {
    val storedValues:HashMap<String, Int> = HashMap()
    val lines = File("./src/input.txt").readLines()
    val regex = "([a-z]+) (inc|dec) (-?\\d+) if ([a-z]+) (!?>?<?=?=?) (-?\\d+)".toRegex()
    for(line in lines){
        val matches = regex.findAll(line).map{ result -> result.groupValues.subList(1, result.groupValues.size) }.toList().get(0)
        val register = matches.get(0)
        if(!storedValues.contains(register)){
            storedValues.put(register, 0)
        }
        val instruction = matches.get(1)
        var amount = matches.get(2).toInt()
        var var1:Int = getValue(matches.get(3),storedValues)
        val expression = matches.get(4)
        var var2 = getValue(matches.get(5),storedValues)
        if(evaluateExpression(var1,expression,var2)){
            addValue(register, instruction, amount, storedValues)
        }
    }
    println(storedValues)
    val maxes = getMax(storedValues)
    println(maxes.first + " " + maxes.second)
}
fun getMax(dict:HashMap<String,Int>): Pair<String,Int>{
    var max = 0
    var maxKey:String = ""
    for(entry in dict.entries){
        if(entry.value > max){
            max = entry.value
            maxKey = entry.key
        }
    }
    return Pair(maxKey,max)
}
fun addValue(id:String,instruction:String,amount: Int, storedValues:HashMap<String,Int>): Unit {
    var amountoAdd:Int = amount
    if(instruction == "dec"){
        amountoAdd = -amount
    }
    if(!storedValues.containsKey(id)){
        throw Exception("HashMap does not contain value")
    }
    storedValues.put(id, storedValues.getValue(id) + amountoAdd)
}
fun getValue(variable:String, storedValues:HashMap<String,Int>): Int{
    var variableAsInt:Int
    if(variable.toIntOrNull() == null){
        if(!storedValues.containsKey(variable)){
            storedValues.put(variable,0)
        }
        variableAsInt = storedValues.get(variable)!!
    }else{
        variableAsInt = variable.toInt()
    }
    return variableAsInt
}
fun evaluateExpression(var1:Int, expression: String, var2:Int): Boolean{
    if(expression == ">="){
        return ge(var1, var2)
    }
    if(expression == "<="){
        return le(var1,var2)
    }
    if(expression == "=="){
        return ee(var1, var2)
    }
    if(expression == "<"){
        return l(var1, var2)
    }
    if(expression == ">"){
        return g(var1,var2)
    }
    if(expression == "!="){
        return ne(var1, var2)
    }
    throw Exception("Did not find match!")
}
fun ge(var1: Int, var2: Int): Boolean{
    return var1 >= var2
}
fun le(var1: Int, var2: Int): Boolean{
    return var1 <= var2
}
fun ee(var1: Int, var2: Int): Boolean{
    return var1 == var2
}
fun ne(var1: Int, var2: Int): Boolean{
    return var1 != var2
}
fun l(var1: Int, var2: Int): Boolean{
    return var1 < var2
}
fun g(var1: Int, var2: Int): Boolean{
    return var1 > var2
}

@Test
fun evaluateFalseStatement() {
    val truthStatement = evaluateExpression(1,"==",2)
    assertFalse { truthStatement }
}
@Test
fun evaluateTrueStatement(){
    val truthStatement = evaluateExpression(1,"==",1)
    assertTrue{truthStatement}
}
@Test
fun evaluateGt(){
    val truthStatement = evaluateExpression(2,">",1)
    assertTrue{truthStatement}
}
@Test
fun evaluateLt(){
    val truthStatement = evaluateExpression(1,"<",2)
    assertTrue{truthStatement}
}
@Test
fun evaluateGte(){
    val truthStatement = evaluateExpression(2,">=",1)
    assertTrue{truthStatement}
}
@Test
fun evaluateLte(){
    val truthStatement = evaluateExpression(1,"<=",2)
    assertTrue{truthStatement}
}
@Test
fun evaluateee(){
    val truthStatement = evaluateExpression(2,"<=",2)
    assertTrue{truthStatement}
}
@Test
fun evaluatne(){
    val truthStatement = evaluateExpression(2,"!=",3)
    assertTrue{truthStatement}
}
@Test
fun notEvaluateTrueStatement(){
    val truthStatement = evaluateExpression(2,"==",1)
    assertFalse{truthStatement}
}
@Test
fun notEvaluateGt(){
    val truthStatement = evaluateExpression(2,">",3)
    assertFalse{truthStatement}
}
@Test
fun notEvaluateLt(){
    val truthStatement = evaluateExpression(3,"<",2)
    assertFalse{truthStatement}
}
@Test
fun notEvaluateGte(){
    val truthStatement = evaluateExpression(2,">=",3)
    assertFalse{truthStatement}
}
@Test
fun notEvaluateLte(){
    val truthStatement = evaluateExpression(3,"<=",2)
    assertFalse{truthStatement}
}
@Test
fun notEvaluateee(){
    val truthStatement = evaluateExpression(3,"<=",2)
    assertFalse{truthStatement}
}
@Test
fun notEvaluatne(){
    val truthStatement = evaluateExpression(3,"!=",3)
    assertFalse{truthStatement}
}
@Test
fun addValueTestAdd(){
    var values:HashMap<String,Int> = HashMap()
    values.put("a",1)
    addValue("a","inc",5,values)
    assertTrue{values.get("a") == 6}
}
@Test
fun addValueTestMinus(){
    var values:HashMap<String,Int> = HashMap()
    values.put("a",1)
    addValue("a","dec",5,values)
    assertTrue{values.get("a") == -4}
}
@Test
fun getValueTestWithString(){
    var values:HashMap<String,Int> = HashMap()
    values.put("a",3)
    val valueFromMap = getValue("a", values)
    assert(valueFromMap == 3)
}
@Test
fun getValueTestWithInt(){
    var values:HashMap<String,Int> = HashMap()
    val valueFromMap = getValue("3", values)
    assert(valueFromMap == 3)
}
@Test
fun getValueTestWithNewValue(){
    var values:HashMap<String,Int> = HashMap()
    val valueFromMap = getValue("a", values)
    assert(valueFromMap == 0)
}
