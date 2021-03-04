package fr.gwendal_jouneaux.rob_lang.interpreter

import fr.gwendal_jouneaux.rob_lang.interpreter.runtime.FunctionReturn
import fr.gwendal_jouneaux.rob_lang.interpreter.runtime.LoopBreak
import fr.gwendal_jouneaux.rob_lang.interpreter.runtime.RuntimeArray
import fr.gwendal_jouneaux.rob_lang.robLANG.And
import fr.gwendal_jouneaux.rob_lang.robLANG.ArrayAdd
import fr.gwendal_jouneaux.rob_lang.robLANG.ArrayGet
import fr.gwendal_jouneaux.rob_lang.robLANG.ArrayLength
import fr.gwendal_jouneaux.rob_lang.robLANG.ArrayNew
import fr.gwendal_jouneaux.rob_lang.robLANG.ArrayRemove
import fr.gwendal_jouneaux.rob_lang.robLANG.ArraySet
import fr.gwendal_jouneaux.rob_lang.robLANG.Assignment
import fr.gwendal_jouneaux.rob_lang.robLANG.Block
import fr.gwendal_jouneaux.rob_lang.robLANG.BoolConstant
import fr.gwendal_jouneaux.rob_lang.robLANG.Break
import fr.gwendal_jouneaux.rob_lang.robLANG.ComplexFunction
import fr.gwendal_jouneaux.rob_lang.robLANG.Condition
import fr.gwendal_jouneaux.rob_lang.robLANG.Divide
import fr.gwendal_jouneaux.rob_lang.robLANG.DoubleConstant
import fr.gwendal_jouneaux.rob_lang.robLANG.Equality
import fr.gwendal_jouneaux.rob_lang.robLANG.Expression
import fr.gwendal_jouneaux.rob_lang.robLANG.FunCall
import fr.gwendal_jouneaux.rob_lang.robLANG.FunDefinition
import fr.gwendal_jouneaux.rob_lang.robLANG.FunParamCapture
import fr.gwendal_jouneaux.rob_lang.robLANG.FunParamExp
import fr.gwendal_jouneaux.rob_lang.robLANG.Greater
import fr.gwendal_jouneaux.rob_lang.robLANG.GreaterEq
import fr.gwendal_jouneaux.rob_lang.robLANG.Inequality
import fr.gwendal_jouneaux.rob_lang.robLANG.InlineFunction
import fr.gwendal_jouneaux.rob_lang.robLANG.IntConstant
import fr.gwendal_jouneaux.rob_lang.robLANG.Less
import fr.gwendal_jouneaux.rob_lang.robLANG.LessEq
import fr.gwendal_jouneaux.rob_lang.robLANG.Loop
import fr.gwendal_jouneaux.rob_lang.robLANG.Minus
import fr.gwendal_jouneaux.rob_lang.robLANG.MoveBackward
import fr.gwendal_jouneaux.rob_lang.robLANG.MoveForward
import fr.gwendal_jouneaux.rob_lang.robLANG.Multiply
import fr.gwendal_jouneaux.rob_lang.robLANG.Not
import fr.gwendal_jouneaux.rob_lang.robLANG.Or
import fr.gwendal_jouneaux.rob_lang.robLANG.Plus
import fr.gwendal_jouneaux.rob_lang.robLANG.PrintExpression
import fr.gwendal_jouneaux.rob_lang.robLANG.Return
import fr.gwendal_jouneaux.rob_lang.robLANG.Robot
import fr.gwendal_jouneaux.rob_lang.robLANG.SenseBattery
import fr.gwendal_jouneaux.rob_lang.robLANG.SenseCompass
import fr.gwendal_jouneaux.rob_lang.robLANG.SenseDistance
import fr.gwendal_jouneaux.rob_lang.robLANG.SensePosition
import fr.gwendal_jouneaux.rob_lang.robLANG.SenseTime
import fr.gwendal_jouneaux.rob_lang.robLANG.SetSpeed
import fr.gwendal_jouneaux.rob_lang.robLANG.Statement
import fr.gwendal_jouneaux.rob_lang.robLANG.StringConstant
import fr.gwendal_jouneaux.rob_lang.robLANG.SymbolRef
import fr.gwendal_jouneaux.rob_lang.robLANG.TurnLeft
import fr.gwendal_jouneaux.rob_lang.robLANG.TurnRight
import fr.gwendal_jouneaux.rob_lang.robLANG.Variable
import fr.gwendal_jouneaux.rob_lang.robLANG.impl.RobLANGFactoryImpl
import fr.gwendal_jouneaux.rob_lang.typing.RobLANGTypeComputer
import java.util.HashMap

class InterpretRobLANG {
 	final RobLANGTypeComputer typer = new RobLANGTypeComputer()

	// CONSTANT
	def dispatch Object interpret(IntConstant e,ContextRobLANG context) {
		e.value
	}
	def dispatch Object interpret(BoolConstant e,ContextRobLANG context) {
		Boolean::parseBoolean(e.value)
	}
	def dispatch Object interpret(StringConstant e,ContextRobLANG context) {
		e.value
	}
	def dispatch Object interpret(DoubleConstant e,ContextRobLANG context) {
		e.value
	}
	
	// ARITHMETIC
	
	def dispatch Object interpret(Multiply e,ContextRobLANG context) {
		if (typer.isDoubleType(typer.typeFor(e))) {
			return (e.left.interpret(context) as Double) * (e.right.interpret(context) as Double)
		} else {
			return (e.left.interpret(context) as Integer) * (e.right.interpret(context) as Integer)
		}
	}
	
	def dispatch Object interpret(Divide e,ContextRobLANG context) {
		if (typer.isDoubleType(typer.typeFor(e))) {
			return (e.left.interpret(context) as Double) / (e.right.interpret(context) as Double)
		} else {
			return (e.left.interpret(context) as Integer) / (e.right.interpret(context) as Integer)
		}
	}
	
	def dispatch Object interpret(Minus e,ContextRobLANG context) {
		if (typer.isDoubleType(typer.typeFor(e))) {
			return (e.left.interpret(context) as Double) - (e.right.interpret(context) as Double)
		} else {
			return (e.left.interpret(context) as Integer) - (e.right.interpret(context) as Integer)
		}
	}
	def dispatch Object interpret(Plus e,ContextRobLANG context) {
		if (typer.isStringType(typer.typeFor(e.left)) || typer.isStringType(typer.typeFor(e.right))){
			return e.left.interpret(context).toString + e.right.interpret(context).toString
		} else if (typer.isDoubleType(typer.typeFor(e))) {
			return (e.left.interpret(context) as Double) + (e.right.interpret(context) as Double)
		} else {
			return (e.left.interpret(context) as Integer) + (e.right.interpret(context) as Integer)
		}
	}
	
	// BOOLEAN
	def dispatch Object interpret(Not e,ContextRobLANG context) {
		!(e.expression.interpret(context) as Boolean)
	}
	def dispatch Object interpret(And e,ContextRobLANG context) {
		(e.left.interpret(context) as Boolean) && (e.right.interpret(context) as Boolean)
	}
	def dispatch Object interpret(Or e,ContextRobLANG context) {
		(e.left.interpret(context) as Boolean) || (e.right.interpret(context) as Boolean)
	}
	
	// COMPARISON
	def dispatch Object interpret(Equality e,ContextRobLANG context) {
		e.left.interpret(context) == e.right.interpret(context)
	}
	def dispatch Object interpret(Inequality e,ContextRobLANG context) {
		e.left.interpret(context) != e.right.interpret(context)
	}
	
	def dispatch Object interpret(GreaterEq e,ContextRobLANG context) {
		if (typer.isStringType(typer.typeFor(e.left))) {
			val left = e.left.interpret(context) as String
			val right = e.right.interpret(context) as String
			return left >= right
		} else {
			val left = e.left.interpret(context) as Double
			val right = e.right.interpret(context) as Double
			return left >= right
		}
	}
	
	def dispatch Object interpret(Greater e,ContextRobLANG context) {
		if (typer.isStringType(typer.typeFor(e.left))) {
			val left = e.left.interpret(context) as String
			val right = e.right.interpret(context) as String
			return left > right
		} else {
			val left = e.left.interpret(context) as Double
			val right = e.right.interpret(context) as Double
			return left > right
		}
	}
	
	def dispatch Object interpret(LessEq e,ContextRobLANG context) {
		if (typer.isStringType(typer.typeFor(e.left))) {
			val left = e.left.interpret(context) as String
			val right = e.right.interpret(context) as String
			return left <= right
		} else {
			val left = e.left.interpret(context) as Double
			val right = e.right.interpret(context) as Double
			return left <= right
		}
	}
	
	def dispatch Object interpret(Less e,ContextRobLANG context) {
		if (typer.isStringType(typer.typeFor(e.left))) {
			val left = e.left.interpret(context) as String
			val right = e.right.interpret(context) as String
			return left < right
		} else {
			val left = e.left.interpret(context) as Double
			val right = e.right.interpret(context) as Double
			return left < right
		}
	}
	
	// VARIABLE AND FUNCTION
	def dispatch Object interpret(SymbolRef e,ContextRobLANG context) {
		if(typer.isStringType(typer.typeFor(e.variable))) return context.get(e.variable.name) as String
		if(typer.isIntType(typer.typeFor(e.variable))) return context.get(e.variable.name) as Integer
		if(typer.isBoolType(typer.typeFor(e.variable))) return context.get(e.variable.name) as Boolean
		if(typer.isDoubleType(typer.typeFor(e.variable))) return context.get(e.variable.name) as Double
		if(typer.isArrayType(typer.typeFor(e.variable))) return context.get(e.variable.name) as RuntimeArray
		context.get(e.variable.name)
	}
	
	def dispatch Object interpret(FunCall e,ContextRobLANG context) {
		val function = e.function
		
		// create the new context with param
		var params = new HashMap<String, Object>()
		for(var i = 0; i<function.varNames.length; i++){
			params.put(function.varNames.get(i).name,e.params.get(i).interpret(context))
		}
		context.pushContext(params)
		
		// interpret
		var res = null as Object
		if(function instanceof InlineFunction){
			res = function.expression.interpret(context)
		}
		if(function instanceof ComplexFunction){
			try{
				res = function.body.interpret(context)
			} catch (FunctionReturn ret){
				res = ret.toReturn
			}
		}
		
		// update captured value in the caller context
		var captured = new HashMap<String, Object>()
		for(var i = 0; i<e.params.length; i++){
			val param = e.params.get(i)
			if(param instanceof FunParamCapture){
				captured.put(param.variable.name,context.get(function.varNames.get(i).name))	
			}
		}
		
		context.popContext()
		context.addAll(captured)
		
		return res
	}
	
	def dispatch Object interpret(FunParamCapture e,ContextRobLANG context) {
		context.get(e.variable.name)
	}
	
	def dispatch Object interpret(FunParamExp e,ContextRobLANG context) {
		e.expr.interpret(context)
	}
	
	def dispatch Object interpret(Block e, ContextRobLANG context) {
		var res = null as Object
		for(var i = 0; i < e.statements.length; i++){
			res = interpret(e.statements.get(i),context)
		}
		return res
	}
	
	def dispatch Object interpret(Break e,ContextRobLANG context) {
		throw new LoopBreak()
	}
	
	def dispatch Object interpret(Return e,ContextRobLANG context) {
		throw new FunctionReturn(e.expression.interpret(context))
	}
	
	// SENSORS
	
	def dispatch Object interpret(SenseTime e,ContextRobLANG context) {
		System.currentTimeMillis
	}
	
	def dispatch Object interpret(SenseBattery e,ContextRobLANG context) {
		context.getBattery
	}
	
	def dispatch Object interpret(SensePosition e,ContextRobLANG context) {
		val array = new RuntimeArray(1)
		val pos = context.getPosition()
		for (axis : pos) {
			array.add(axis)
		}
		return array
	}
	
	def dispatch Object interpret(SenseDistance e,ContextRobLANG context) {
		context.getDistance(e.sensorIndex)
	}
	
	def dispatch Object interpret(SenseCompass e,ContextRobLANG context) {
		context.getCompass
	}
	
	
	def dispatch Object interpret(Expression e,ContextRobLANG context) {
		throw new Exception("AST Node not dispatched")
	}
	
	
	
	def dispatch Object interpret(Variable e,ContextRobLANG context) {
		val value = e.expression.interpret(context)
		context.put(e.name, value)
		return value
	}
	
	def dispatch Object interpret(Assignment e,ContextRobLANG context) {
		val value = e.expression.interpret(context)
		context.put(e.assignee.name, value)
		return value
	}
	
	def dispatch Object interpret(PrintExpression e,ContextRobLANG context) {
		val value = e.expression.interpret(context)
		println(value)
		return value
	}
	
	def dispatch Object interpret(Condition e,ContextRobLANG context) {
		var cond = e.expression.interpret(context) as Boolean
		if(cond){
			return e.ifz.interpret(context)
		}else if(e.elsez !== null){
			return e.elsez.interpret(context)
		}
	}
	
	def dispatch Object interpret(Loop e,ContextRobLANG context) {
		var last = null as Object
		try{
			while(e.expression.interpret(context) as Boolean){
				last = e.body.interpret(context)
			}
		} catch(LoopBreak brk) {
			last = null as Object
		}
		
		return last
	}
	
	// MOVEMENTS
	def dispatch Object interpret(MoveForward e,ContextRobLANG context) {
		val dist = e.duration.interpret(context) as Integer
		context.moveRobot(dist)
		return null
	}
	
	def dispatch Object interpret(MoveBackward e,ContextRobLANG context) {
		val dist = e.duration.interpret(context) as Integer
		context.moveRobot(- dist)
		return null
	}
	
	def dispatch Object interpret(TurnLeft e,ContextRobLANG context) {
		val angle = e.duration.interpret(context) as Integer
		context.turnRobot(angle)
		return null
	}
	
	def dispatch Object interpret(TurnRight e,ContextRobLANG context) {
		val angle = e.duration.interpret(context) as Integer
		context.turnRobot(- angle)
		return null
	}
	
	def dispatch Object interpret(SetSpeed e,ContextRobLANG context) {
		context.setSpeed(e.speed.interpret(context) as Double)
		return null
	}
	
	
	// Arrays
	
	def dispatch Object interpret(ArrayNew e,ContextRobLANG context) {
		return new RuntimeArray(e.dimension)
	}
	
	def dispatch Object interpret(ArrayLength e,ContextRobLANG context) {
		val a = e.array.interpret(context) as RuntimeArray
		return a.length
	}
	
	def dispatch Object interpret(ArrayGet e,ContextRobLANG context) {
		val a = e.array.interpret(context) as RuntimeArray
		return a.get(e.index.interpret(context) as Integer)
	}
	
	def dispatch Object interpret(ArraySet e,ContextRobLANG context) {
		val a = e.array.interpret(context) as RuntimeArray
		a.set(e.index.interpret(context) as Integer, e.value.interpret(context))
		return null
	}
	
	def dispatch Object interpret(ArrayAdd e,ContextRobLANG context) {
		val a = e.array.interpret(context) as RuntimeArray
		if(e.indexedValue === null){
			a.add(e.valueOrIndex.interpret(context))
		} else {
			a.add(e.valueOrIndex.interpret(context) as Integer, e.indexedValue.interpret(context))
		}
		return null
	}
	
	def dispatch Object interpret(ArrayRemove e,ContextRobLANG context) {
		val a = e.array.interpret(context) as RuntimeArray
		a.remove(e.index.interpret(context) as Integer)
		return null
	}
	

	def dispatch Object interpret(Statement e, ContextRobLANG context) {
		
	}
	
	def dispatch Object interpret(Robot e, ContextRobLANG context) {
		var res = null as Object
		for(var i = 0; i < e.functions.length; i++){
			var f = e.functions.get(i) as FunDefinition
			if(f.name == "Main"){
				val callMain = RobLANGFactoryImpl.eINSTANCE.createFunCall
				callMain.function = f
				res = interpret(callMain,context)
			}
		}
		return res
	}
}