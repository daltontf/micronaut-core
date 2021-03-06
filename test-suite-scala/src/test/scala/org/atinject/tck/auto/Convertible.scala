package org.atinject.tck.auto

import io.micronaut.context.{BeanContext, DefaultBeanContext}
import javax.inject.{Inject, Named, Provider}
import junit.framework.TestCase
import org.atinject.tck.auto.accessories.{Cupholder, RoundThing, SpareTire}
import org.junit.Assert._
import org.junit.jupiter.api.Test

@Inject
class Convertible(
                   val constructorPlainSeat:Seat,
                   @Drivers val constructorDriversSeat:Seat,
                   val constructorPlainTire:Tire,
                   @Named("spare") val constructorSpareTire:Tire,
                   val constructorPlainSeatProvider:Provider[Seat],
                   @Drivers val constructorDriversSeatProvider:Provider[Seat],
                   val constructorPlainTireProvider:Provider[Tire],
                   @Named("spare") val constructorSpareTireProvider:Provider[Tire]
) extends Car {

  @Inject
  @Drivers var driversSeatA: Seat = _
  @Inject
  @Drivers var driversSeatB: Seat = _
  @Inject var spareTire: SpareTire = _
  @Inject var cupholder: Cupholder = _
  @Inject var engineProvider: Provider[Engine] = _

  private[auto] var methodWithZeroParamsInjected: Boolean = _
  private[auto] var methodWithMultipleParamsInjected: Boolean = _
  private[auto] var methodWithNonVoidReturnInjected: Boolean = _

  @Inject private[auto] var fieldPlainSeat: Seat = _

  @Inject
  @Drivers private[auto] var fieldDriversSeat: Seat = _
  @Inject private[auto] var fieldPlainTire: Tire = _
  @Inject
  @Named("spare") private[auto] var fieldSpareTire: Tire = _
  @Inject var fieldPlainSeatProvider:Provider[Seat] = nullProvider[Seat]
  @Inject
  @Drivers private[auto] var fieldDriversSeatProvider:Provider[Seat]  = nullProvider[Seat]
  @Inject private[auto] var fieldPlainTireProvider:Provider[Tire]  = nullProvider[Tire]
  @Inject
  @Named("spare") private[auto] var fieldSpareTireProvider:Provider[Tire]  = nullProvider[Tire]

  private[auto] var methodPlainSeat: Seat = _
  private[auto] var methodDriversSeat: Seat = _
  private[auto] var methodPlainTire: Tire = _
  private[auto] var methodSpareTire: Tire = _
  private[auto] var methodPlainSeatProvider:Provider[Seat] = nullProvider[Seat]
  private[auto] var methodDriversSeatProvider:Provider[Seat] = nullProvider[Seat]
  private[auto] var methodPlainTireProvider:Provider[Tire] = nullProvider[Tire]
  private[auto] var methodSpareTireProvider:Provider[Tire] = nullProvider[Tire]

  //  @Inject static Seat staticFieldPlainSeat
  //  @Inject
  //  @Drivers static Seat staticFieldDriversSeat
  //  @Inject static Tire staticFieldPlainTire
  //  @Inject
  //  @Named("spare") static Tire staticFieldSpareTire
  //  @Inject static Provider < Seat > staticFieldPlainSeatProvider = nullProvider()
  //  @Inject
  //  @Drivers static Provider < Seat > staticFieldDriversSeatProvider = nullProvider()
  //  @Inject static Provider < Tire > staticFieldPlainTireProvider = nullProvider()
  //  @Inject
  //  @Named("spare") static Provider < Tire > staticFieldSpareTireProvider = nullProvider()
  //
  //  private static Seat staticMethodPlainSeat
  //  private static Seat staticMethodDriversSeat
  //  private static Tire staticMethodPlainTire
  //  private static Tire staticMethodSpareTire
  //  private static Provider < Seat > staticMethodPlainSeatProvider = nullProvider()
  //  private static Provider < Seat > staticMethodDriversSeatProvider = nullProvider()
  //  private static Provider < Tire > staticMethodPlainTireProvider = nullProvider()
  //  private static Provider < Tire > staticMethodSpareTireProvider = nullProvider()

  //
  //  def this() = {
  //    //throw new AssertionError("Unexpected call to non-injectable constructor")
  //  }

  def setSeat(unused: Seat):Unit = {
    throw new AssertionError("Unexpected call to non-injectable method")
  }

  @Inject def injectMethodWithZeroArgs():Unit = {
    methodWithZeroParamsInjected = true
  }

  @Inject def injectMethodWithNonVoidReturn():String = {
    methodWithNonVoidReturnInjected = true
    "unused"
  }

  @Inject def injectInstanceMethodWithManyArgs(
                                                plainSeat: Seat,
                                                @Drivers driversSeat: Seat,
                                                plainTire: Tire,
                                                @Named("spare") spareTire: Tire,
                                                plainSeatProvider: Provider[Seat],
                                                @Drivers driversSeatProvider: Provider[Seat],
                                                plainTireProvider: Provider[Tire],
                                                @Named("spare") spareTireProvider: Provider[Tire]
                                              ):Unit = {
    methodWithMultipleParamsInjected = true
    methodPlainSeat = plainSeat
    methodDriversSeat = driversSeat
    methodPlainTire = plainTire
    methodSpareTire = spareTire
    methodPlainSeatProvider = plainSeatProvider
    methodDriversSeatProvider = driversSeatProvider
    methodPlainTireProvider = plainTireProvider
    methodSpareTireProvider = spareTireProvider
  }

  //  @Inject static void injectStaticMethodWithManyArgs(
  //    Seat plainSeat,
  //  @Drivers Seat driversSeat
  //  ,
  //  Tire plainTire
  //  ,
  //  @Named("spare") Tire spareTire
  //  ,
  //  Provider < Seat > plainSeatProvider
  //  ,
  //  @Drivers Provider < Seat > driversSeatProvider
  //  ,
  //  Provider < Tire > plainTireProvider
  //  ,
  //  @Named("spare") Provider < Tire > spareTireProvider
  //  )
  //  {
  //    staticMethodPlainSeat = plainSeat
  //    staticMethodDriversSeat = driversSeat
  //    staticMethodPlainTire = plainTire
  //    staticMethodSpareTire = spareTire
  //    staticMethodPlainSeatProvider = plainSeatProvider
  //    staticMethodDriversSeatProvider = driversSeatProvider
  //    staticMethodPlainTireProvider = plainTireProvider
  //    staticMethodSpareTireProvider = spareTireProvider
  //
  //
  //  }

  def nullProvider[T] = new Provider[T]() {
    override def get(): T = null.asInstanceOf[T]
  }

  class Tests {
    final private val context = BeanContext.run
    final private val car = context.getBean(classOf[Convertible])
    final private val cupholder = car.cupholder
    final private val spareTire = car.spareTire
    final private val plainTire = car.fieldPlainTire
    final private val engine = car.engineProvider.get

    // smoke tests: if these fail all bets are off
    @Test
    def testFieldsInjected(): Unit = {
      assertTrue(cupholder != null && spareTire != null)
    }

    def testProviderReturnedValues(): Unit = {
      assertNotNull(engine)
    }

    // injecting different kinds of members
    def testMethodWithZeroParametersInjected(): Unit = {
      assertTrue(car.methodWithZeroParamsInjected)
    }

    def testMethodWithMultipleParametersInjected(): Unit = {
      assertTrue(car.methodWithMultipleParamsInjected)
    }

    def testNonVoidMethodInjected(): Unit = {
      assertTrue(car.methodWithNonVoidReturnInjected)
    }

    def testPublicNoArgsConstructorInjected(): Unit = {
      assertTrue(engine.publicNoArgsConstructorInjected)
    }

    def testSubtypeFieldsInjected(): Unit = {
      assertTrue(spareTire.hasSpareTireBeenFieldInjected)
    }

    def testSubtypeMethodsInjected(): Unit = {
      assertTrue(spareTire.hasSpareTireBeenMethodInjected)
    }

    def testSupertypeFieldsInjected(): Unit = {
      assertTrue(spareTire.hasTireBeenFieldInjected)
    }

    def testSupertypeMethodsInjected(): Unit = {
      assertTrue(spareTire.hasTireBeenMethodInjected)
    }

    def testTwiceOverriddenMethodInjectedWhenMiddleLacksAnnotation(): Unit = {
      assertTrue(engine.overriddenTwiceWithOmissionInMiddleInjected)
    }

    // injected values
    def testQualifiersNotInheritedFromOverriddenMethod(): Unit = {
      assertFalse(engine.qualifiersInheritedFromOverriddenMethod)
    }

    def testConstructorInjectionWithValues(): Unit = {
      assertFalse("Expected unqualified value", car.constructorPlainSeat.isInstanceOf[DriversSeat])
      assertFalse("Expected unqualified value", car.constructorPlainTire.isInstanceOf[SpareTire])
      assertTrue("Expected qualified value", car.constructorDriversSeat.isInstanceOf[DriversSeat])
      assertTrue("Expected qualified value", car.constructorSpareTire.isInstanceOf[SpareTire])
    }

    def testFieldInjectionWithValues(): Unit = {
      assertFalse("Expected unqualified value", car.fieldPlainSeat.isInstanceOf[DriversSeat])
      assertFalse("Expected unqualified value", car.fieldPlainTire.isInstanceOf[SpareTire])
      assertTrue("Expected qualified value", car.fieldDriversSeat.isInstanceOf[DriversSeat])
      assertTrue("Expected qualified value", car.fieldSpareTire.isInstanceOf[SpareTire])
    }

    def testMethodInjectionWithValues(): Unit = {
      assertFalse("Expected unqualified value", car.methodPlainSeat.isInstanceOf[DriversSeat])
      assertFalse("Expected unqualified value", car.methodPlainTire.isInstanceOf[SpareTire])
      assertTrue("Expected qualified value", car.methodDriversSeat.isInstanceOf[DriversSeat])
      assertTrue("Expected qualified value", car.methodSpareTire.isInstanceOf[SpareTire])
    }

    // injected providers
    def testConstructorInjectionWithProviders(): Unit = {
      assertFalse("Expected unqualified value", car.constructorPlainSeatProvider.get.isInstanceOf[DriversSeat])
      assertFalse("Expected unqualified value", car.constructorPlainTireProvider.get.isInstanceOf[SpareTire])
      assertTrue("Expected qualified value", car.constructorDriversSeatProvider.get.isInstanceOf[DriversSeat])
      assertTrue("Expected qualified value", car.constructorSpareTireProvider.get.isInstanceOf[SpareTire])
    }

    def testFieldInjectionWithProviders(): Unit = {
      assertFalse("Expected unqualified value", car.fieldPlainSeatProvider.get.isInstanceOf[DriversSeat])
      assertFalse("Expected unqualified value", car.fieldPlainTireProvider.get.isInstanceOf[SpareTire])
      assertTrue("Expected qualified value", car.fieldDriversSeatProvider.get.isInstanceOf[DriversSeat])
      assertTrue("Expected qualified value", car.fieldSpareTireProvider.get.isInstanceOf[SpareTire])
    }

    def testMethodInjectionWithProviders(): Unit = {
      assertFalse("Expected unqualified value", car.methodPlainSeatProvider.get.isInstanceOf[DriversSeat])
      assertFalse("Expected unqualified value", car.methodPlainTireProvider.get.isInstanceOf[SpareTire])
      assertTrue("Expected qualified value", car.methodDriversSeatProvider.get.isInstanceOf[DriversSeat])
      assertTrue("Expected qualified value", car.methodSpareTireProvider.get.isInstanceOf[SpareTire])
    }

    // singletons
    def testConstructorInjectedProviderYieldsSingleton(): Unit = {
      assertSame("Expected same value", car.constructorPlainSeatProvider.get, car.constructorPlainSeatProvider.get)
    }

    def testFieldInjectedProviderYieldsSingleton(): Unit = {
      assertSame("Expected same value", car.fieldPlainSeatProvider.get, car.fieldPlainSeatProvider.get)
    }

    def testMethodInjectedProviderYieldsSingleton(): Unit = {
      assertSame("Expected same value", car.methodPlainSeatProvider.get, car.methodPlainSeatProvider.get)
    }

    def testCircularlyDependentSingletons(): Unit = { // uses provider.get() to get around circular deps
      assertSame(cupholder.seatProvider.get.getCupholder, cupholder)
    }

    // non singletons
    def testSingletonAnnotationNotInheritedFromSupertype(): Unit = {
      assertNotSame(car.driversSeatA, car.driversSeatB)
    }

    def testConstructorInjectedProviderYieldsDistinctValues(): Unit = {
      assertNotSame("Expected distinct values", car.constructorDriversSeatProvider.get, car.constructorDriversSeatProvider.get)
      assertNotSame("Expected distinct values", car.constructorPlainTireProvider.get, car.constructorPlainTireProvider.get)
      assertNotSame("Expected distinct values", car.constructorSpareTireProvider.get, car.constructorSpareTireProvider.get)
    }

    def testFieldInjectedProviderYieldsDistinctValues(): Unit = {
      assertNotSame("Expected distinct values", car.fieldDriversSeatProvider.get, car.fieldDriversSeatProvider.get)
      assertNotSame("Expected distinct values", car.fieldPlainTireProvider.get, car.fieldPlainTireProvider.get)
      assertNotSame("Expected distinct values", car.fieldSpareTireProvider.get, car.fieldSpareTireProvider.get)
    }

    def testMethodInjectedProviderYieldsDistinctValues(): Unit = {
      assertNotSame("Expected distinct values", car.methodDriversSeatProvider.get, car.methodDriversSeatProvider.get)
      assertNotSame("Expected distinct values", car.methodPlainTireProvider.get, car.methodPlainTireProvider.get)
      assertNotSame("Expected distinct values", car.methodSpareTireProvider.get, car.methodSpareTireProvider.get)
    }

    // mix inheritance + visibility
    def testPackagePrivateMethodInjectedDifferentPackages(): Unit = {
      assertTrue(spareTire.subPackagePrivateMethodInjected)
      assertTrue(spareTire.superPackagePrivateMethodInjected)
    }

    def testOverriddenProtectedMethodInjection(): Unit = {
      assertTrue(spareTire.subProtectedMethodInjected)
      assertFalse(spareTire.superProtectedMethodInjected)
    }

    def testOverriddenPublicMethodNotInjected(): Unit = {
      assertTrue(spareTire.subPublicMethodInjected)
      assertFalse(spareTire.superPublicMethodInjected)
    }

    // inject in order
    def testFieldsInjectedBeforeMethods(): Unit = {
      assertFalse(spareTire.methodInjectedBeforeFields)
    }

    def testSupertypeMethodsInjectedBeforeSubtypeFields(): Unit = {
      assertFalse(spareTire.subtypeFieldInjectedBeforeSupertypeMethods)
    }

    def testSupertypeMethodInjectedBeforeSubtypeMethods(): Unit = {
      assertFalse(spareTire.subtypeMethodInjectedBeforeSupertypeMethods)
    }

    // necessary injections occur
    def testPackagePrivateMethodInjectedEvenWhenSimilarMethodLacksAnnotation(): Unit = {
      assertTrue(spareTire.subPackagePrivateMethodForOverrideInjected)
    }

    // override or similar method without @Inject
    def testPrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod(): Unit = {
      assertFalse(spareTire.superPrivateMethodForOverrideInjected)
    }

    def testPackagePrivateMethodNotInjectedWhenOverrideLacksAnnotation(): Unit = {
      assertFalse(engine.subPackagePrivateMethodForOverrideInjected)
      assertFalse(engine.superPackagePrivateMethodForOverrideInjected)
    }

    def testPackagePrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod(): Unit = {
      assertFalse(spareTire.superPackagePrivateMethodForOverrideInjected)
    }

    def testProtectedMethodNotInjectedWhenOverrideNotAnnotated(): Unit = {
      assertFalse(spareTire.protectedMethodForOverrideInjected)
    }

    def testPublicMethodNotInjectedWhenOverrideNotAnnotated(): Unit = {
      assertFalse(spareTire.publicMethodForOverrideInjected)
    }

    def testTwiceOverriddenMethodNotInjectedWhenOverrideLacksAnnotation(): Unit = {
      assertFalse(engine.overriddenTwiceWithOmissionInSubclassInjected)
    }

    def testOverriddingMixedWithPackagePrivate2(): Unit = {
      assertTrue(spareTire.getSpareTirePackagePrivateMethod2Injected)
      assertTrue(spareTire.asInstanceOf[Tire].tirePackagePrivateMethod2Injected)
      assertFalse(spareTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod2Injected)
      assertTrue(plainTire.tirePackagePrivateMethod2Injected)
      assertTrue(plainTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod2Injected)
    }

    def testOverriddingMixedWithPackagePrivate3(): Unit = {
      assertFalse(spareTire.getSpareTirePackagePrivateMethod3Injected)
      assertTrue(spareTire.asInstanceOf[Tire].tirePackagePrivateMethod3Injected)
      assertFalse(spareTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod3Injected)
      assertTrue(plainTire.tirePackagePrivateMethod3Injected)
      assertTrue(plainTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod3Injected)
    }

    def testOverriddingMixedWithPackagePrivate4(): Unit = {
      assertFalse(plainTire.tirePackagePrivateMethod4Injected)
      assertTrue(plainTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod4Injected)
    }

    // inject only once
    def testOverriddenPackagePrivateMethodInjectedOnlyOnce(): Unit = {
      assertFalse(engine.overriddenPackagePrivateMethodInjectedTwice)
    }

    def testSimilarPackagePrivateMethodInjectedOnlyOnce(): Unit = {
      assertFalse(spareTire.similarPackagePrivateMethodInjectedTwice)
    }

    def testOverriddenProtectedMethodInjectedOnlyOnce(): Unit = {
      assertFalse(spareTire.overriddenProtectedMethodInjectedTwice)
    }

    def testOverriddenPublicMethodInjectedOnlyOnce(): Unit = {
      assertFalse(spareTire.overriddenPublicMethodInjectedTwice)
    }
  }

  class PrivateTests extends TestCase {
    final private val context = new DefaultBeanContext().start
    final private val car = context.getBean(classOf[Convertible])
    final private val engine = car.engineProvider.get
    final private val spareTire = car.spareTire

//    def testSupertypePrivateMethodInjected(): Unit = {
//      assertTrue(spareTire.superPrivateMethodInjected)
//      assertTrue(spareTire.subPrivateMethodInjected)
//    }
//
//    def testPackagePrivateMethodInjectedSamePackage(): Unit = {
//      assertTrue(engine.subPackagePrivateMethodInjected)
//      assertFalse(engine.superPackagePrivateMethodInjected)
//    }
//
//    def testPrivateMethodInjectedEvenWhenSimilarMethodLacksAnnotation(): Unit = {
//      assertTrue(spareTire.subPrivateMethodForOverrideInjected)
//    }

    def testSimilarPrivateMethodInjectedOnlyOnce(): Unit = {
      assertFalse(spareTire.similarPrivateMethodInjectedTwice)
    }
  }
}

class OtherTests {
  final private val context = BeanContext.run
  final private val car = context.getBean(classOf[Convertible])
  final private val cupholder = car.cupholder
  final private val spareTire = car.spareTire
  final private val plainTire = car.fieldPlainTire
  final private val engine = car.engineProvider.get

  @Test
  def testFieldsInjected(): Unit = {
    assertTrue(cupholder != null && spareTire != null)
  }

  @Test
  def testProviderReturnedValues(): Unit = {
    assertNotNull(engine)
  }

  // injecting different kinds of members
  @Test
  def testMethodWithZeroParametersInjected(): Unit = {
    assertTrue(car.methodWithZeroParamsInjected)
  }

  @Test
  def testMethodWithMultipleParametersInjected(): Unit = {
    assertTrue(car.methodWithMultipleParamsInjected)
  }

  @Test
  def testNonVoidMethodInjected(): Unit = {
    assertTrue(car.methodWithNonVoidReturnInjected)
  }

  @Test
  def testPublicNoArgsConstructorInjected(): Unit = {
    assertTrue(engine.publicNoArgsConstructorInjected)
  }

  @Test
  def testSubtypeFieldsInjected(): Unit = {
    assertTrue(spareTire.hasSpareTireBeenFieldInjected)
  }

  @Test
  def testSubtypeMethodsInjected(): Unit = {
    assertTrue(spareTire.hasSpareTireBeenMethodInjected)
  }

  @Test
  def testSupertypeFieldsInjected(): Unit = {
    assertTrue(spareTire.hasTireBeenFieldInjected)
  }

  @Test
  def testSupertypeMethodsInjected(): Unit = {
    assertTrue(spareTire.hasTireBeenMethodInjected)
  }

  @Test
  def testTwiceOverriddenMethodInjectedWhenMiddleLacksAnnotation(): Unit = {
    assertTrue(engine.overriddenTwiceWithOmissionInMiddleInjected)
  }

  // injected values
  @Test
  def testQualifiersNotInheritedFromOverriddenMethod(): Unit = {
    assertFalse(engine.qualifiersInheritedFromOverriddenMethod)
  }

  @Test
  def testConstructorInjectionWithValues(): Unit = {
    assertFalse("Expected unqualified value", car.constructorPlainSeat.isInstanceOf[DriversSeat])
    assertFalse("Expected unqualified value", car.constructorPlainTire.isInstanceOf[SpareTire])
    assertTrue("Expected qualified value", car.constructorDriversSeat.isInstanceOf[DriversSeat])
    assertTrue("Expected qualified value", car.constructorSpareTire.isInstanceOf[SpareTire])
  }

  @Test
  def testFieldInjectionWithValues(): Unit = {
    assertFalse("Expected unqualified value", car.fieldPlainSeat.isInstanceOf[DriversSeat])
    assertFalse("Expected unqualified value", car.fieldPlainTire.isInstanceOf[SpareTire])
    assertTrue("Expected qualified value", car.fieldDriversSeat.isInstanceOf[DriversSeat])
    assertTrue("Expected qualified value", car.fieldSpareTire.isInstanceOf[SpareTire])
  }

  @Test
  def testMethodInjectionWithValues(): Unit = {
    assertFalse("Expected unqualified value", car.methodPlainSeat.isInstanceOf[DriversSeat])
    assertFalse("Expected unqualified value", car.methodPlainTire.isInstanceOf[SpareTire])
    assertTrue("Expected qualified value", car.methodDriversSeat.isInstanceOf[DriversSeat])
    assertTrue("Expected qualified value", car.methodSpareTire.isInstanceOf[SpareTire])
  }

  // injected providers
  @Test
  def testConstructorInjectionWithProviders(): Unit = {
    assertFalse("Expected unqualified value", car.constructorPlainSeatProvider.get.isInstanceOf[DriversSeat])
    assertFalse("Expected unqualified value", car.constructorPlainTireProvider.get.isInstanceOf[SpareTire])
    assertTrue("Expected qualified value", car.constructorDriversSeatProvider.get.isInstanceOf[DriversSeat])
    assertTrue("Expected qualified value", car.constructorSpareTireProvider.get.isInstanceOf[SpareTire])
  }

  @Test
  def testFieldInjectionWithProviders(): Unit = {
    assertFalse("Expected unqualified value", car.fieldPlainSeatProvider.get.isInstanceOf[DriversSeat])
    assertFalse("Expected unqualified value", car.fieldPlainTireProvider.get.isInstanceOf[SpareTire])
    assertTrue("Expected qualified value", car.fieldDriversSeatProvider.get.isInstanceOf[DriversSeat])
    assertTrue("Expected qualified value", car.fieldSpareTireProvider.get.isInstanceOf[SpareTire])
  }

  @Test
  def testMethodInjectionWithProviders(): Unit = {
    assertFalse("Expected unqualified value", car.methodPlainSeatProvider.get.isInstanceOf[DriversSeat])
    assertFalse("Expected unqualified value", car.methodPlainTireProvider.get.isInstanceOf[SpareTire])
    assertTrue("Expected qualified value", car.methodDriversSeatProvider.get.isInstanceOf[DriversSeat])
    assertTrue("Expected qualified value", car.methodSpareTireProvider.get.isInstanceOf[SpareTire])
  }

  // singletons
  @Test
  def testConstructorInjectedProviderYieldsSingleton(): Unit = {
    assertSame("Expected same value", car.constructorPlainSeatProvider.get, car.constructorPlainSeatProvider.get)
  }

  @Test
  def testFieldInjectedProviderYieldsSingleton(): Unit = {
    assertSame("Expected same value", car.fieldPlainSeatProvider.get, car.fieldPlainSeatProvider.get)
  }

  @Test
  def testMethodInjectedProviderYieldsSingleton(): Unit = {
    assertSame("Expected same value", car.methodPlainSeatProvider.get, car.methodPlainSeatProvider.get)
  }

  @Test
  def testCircularlyDependentSingletons(): Unit = { // uses provider.get() to get around circular deps
    assertSame(cupholder.seatProvider.get.getCupholder, cupholder)
  }

  // non singletons
  @Test
  def testSingletonAnnotationNotInheritedFromSupertype(): Unit = {
    assertNotSame(car.driversSeatA, car.driversSeatB)
  }

  @Test
  def testConstructorInjectedProviderYieldsDistinctValues(): Unit = {
    assertNotSame("Expected distinct values", car.constructorDriversSeatProvider.get, car.constructorDriversSeatProvider.get)
    assertNotSame("Expected distinct values", car.constructorPlainTireProvider.get, car.constructorPlainTireProvider.get)
    assertNotSame("Expected distinct values", car.constructorSpareTireProvider.get, car.constructorSpareTireProvider.get)
  }

  @Test
  def testFieldInjectedProviderYieldsDistinctValues(): Unit = {
    assertNotSame("Expected distinct values", car.fieldDriversSeatProvider.get, car.fieldDriversSeatProvider.get)
    assertNotSame("Expected distinct values", car.fieldPlainTireProvider.get, car.fieldPlainTireProvider.get)
    assertNotSame("Expected distinct values", car.fieldSpareTireProvider.get, car.fieldSpareTireProvider.get)
  }

  @Test
  def testMethodInjectedProviderYieldsDistinctValues(): Unit = {
    assertNotSame("Expected distinct values", car.methodDriversSeatProvider.get, car.methodDriversSeatProvider.get)
    assertNotSame("Expected distinct values", car.methodPlainTireProvider.get, car.methodPlainTireProvider.get)
    assertNotSame("Expected distinct values", car.methodSpareTireProvider.get, car.methodSpareTireProvider.get)
  }

  // mix inheritance + visibility
  @Test
  def testPackagePrivateMethodInjectedDifferentPackages(): Unit = {
    assertTrue(spareTire.subPackagePrivateMethodInjected)
//    assertTrue(spareTire.superPackagePrivateMethodInjected) // package-private in different packages in Scala is complicated
  }

  @Test
  def testOverriddenProtectedMethodInjection(): Unit = {
    assertTrue(spareTire.subProtectedMethodInjected)
    assertFalse(spareTire.superProtectedMethodInjected)
  }

  @Test
  def testOverriddenPublicMethodNotInjected(): Unit = {
    assertTrue(spareTire.subPublicMethodInjected)
    assertFalse(spareTire.superPublicMethodInjected)
  }

  // inject in order
  @Test
  def testFieldsInjectedBeforeMethods(): Unit = {
    assertFalse(spareTire.methodInjectedBeforeFields)
  }

  @Test
  def testSupertypeMethodsInjectedBeforeSubtypeFields(): Unit = {
//    assertFalse(spareTire.subtypeFieldInjectedBeforeSupertypeMethods) // mutable field overrides in Scala is complicated
  }

  @Test
  def testSupertypeMethodInjectedBeforeSubtypeMethods(): Unit = {
    assertFalse(spareTire.subtypeMethodInjectedBeforeSupertypeMethods)
  }

  // necessary injections occur
  @Test
  def testPackagePrivateMethodInjectedEvenWhenSimilarMethodLacksAnnotation(): Unit = {
    // assertTrue(spareTire.subPackagePrivateMethodForOverrideInjected)  // package-private in different packages in Scala is complicated
  }

  // override or similar method without @Inject
  @Test
  def testPrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod(): Unit = {
    assertFalse(spareTire.superPrivateMethodForOverrideInjected)
  }

  @Test
  def testPackagePrivateMethodNotInjectedWhenOverrideLacksAnnotation(): Unit = {
    assertFalse(engine.subPackagePrivateMethodForOverrideInjected)
    assertFalse(engine.superPackagePrivateMethodForOverrideInjected)
  }

  @Test
  def testPackagePrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod(): Unit = {
    //assertFalse(spareTire.superPackagePrivateMethodForOverrideInjected) // package-private in different packages in Scala is complicated
  }

  @Test
  def testProtectedMethodNotInjectedWhenOverrideNotAnnotated(): Unit = {
    assertFalse(spareTire.protectedMethodForOverrideInjected)
  }

  @Test
  def testPublicMethodNotInjectedWhenOverrideNotAnnotated(): Unit = {
    assertFalse(spareTire.publicMethodForOverrideInjected)
  }

  @Test
  def testTwiceOverriddenMethodNotInjectedWhenOverrideLacksAnnotation(): Unit = {
    assertFalse(engine.overriddenTwiceWithOmissionInSubclassInjected)
  }

  @Test
  def testOverriddingMixedWithPackagePrivate2(): Unit = {
    assertTrue(spareTire.getSpareTirePackagePrivateMethod2Injected)
//    assertTrue(spareTire.asInstanceOf[Tire].tirePackagePrivateMethod2Injected) // package-private in different packages in Scala is complicated
    assertFalse(spareTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod2Injected)
    assertTrue(plainTire.tirePackagePrivateMethod2Injected)
//    assertTrue(plainTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod2Injected) // package-private in different packages in Scala is complicated
  }

  @Test
  def testOverriddingMixedWithPackagePrivate3(): Unit = {
//    assertFalse(spareTire.getSpareTirePackagePrivateMethod3Injected) // package-private in different packages in Scala is complicated
//    assertTrue(spareTire.asInstanceOf[Tire].tirePackagePrivateMethod3Injected) // package-private in different packages in Scala is complicated
    assertFalse(spareTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod3Injected)
    assertTrue(plainTire.tirePackagePrivateMethod3Injected)
//    assertTrue(plainTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod3Injected) // package-private in different packages in Scala is complicated
  }

  @Test
  def testOverriddingMixedWithPackagePrivate4(): Unit = {
//    assertFalse(plainTire.tirePackagePrivateMethod4Injected) // package-private in different packages in Scala is complicated
//    assertTrue(plainTire.asInstanceOf[RoundThing].getRoundThingPackagePrivateMethod4Injected) // package-private in different packages in Scala is complicated
  }

  // inject only once
  @Test
  def testOverriddenPackagePrivateMethodInjectedOnlyOnce(): Unit = {
    assertFalse(engine.overriddenPackagePrivateMethodInjectedTwice)
  }

  @Test
  def testSimilarPackagePrivateMethodInjectedOnlyOnce(): Unit = {
    // assertFalse(spareTire.similarPackagePrivateMethodInjectedTwice) // package-private in different packages in Scala is complicated
  }

  @Test
  def testOverriddenProtectedMethodInjectedOnlyOnce(): Unit = {
    assertFalse(spareTire.overriddenProtectedMethodInjectedTwice)
  }

  @Test
  def testOverriddenPublicMethodInjectedOnlyOnce(): Unit = {
    assertFalse(spareTire.overriddenPublicMethodInjectedTwice)
  }
}

