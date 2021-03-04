package fr.gwendal_jouneaux.rob_lang.interpreter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.eclipse.emf.ecore.EObject;


/**
 * AdaptationAspect : Aspect that create the glue between the interpreter and the adaptation concerns
 * @author gjouneau
 *
 */
@Aspect
public class AdaptationAspect {

	/**
	 * aroundNode : add calls to adaptive modules before and after the semantics execution.
	 * Moreover, allow to shortcut semantics from modules using the AROUND {@link AbstractAdaptationModule.Strategy}
	 * and override the result of the execution
	 * @param pjp : The joinpoint giving access to the context of the code injection(i.e. AST node executed)
	 * @return The value resulting of the computation or an override of this value
	 * @throws Throwable
	 */
	@Around("execution(Object fr.gwendal_jouneaux.rob_lang.interpreter.InterpretRobLANG._interpret(..))")
    public Object aroundNode(ProceedingJoinPoint pjp) throws Throwable {
		EObject node = (EObject) pjp.getArgs()[0];
		
		boolean doTheMethod = FeedbackLoop.updateBefore(node, pjp.getArgs());

		Object out = null;
		if(doTheMethod){
			out = pjp.proceed();
		}
		
		return FeedbackLoop.updateAfter(node, pjp.getArgs(), out);
    }
}
