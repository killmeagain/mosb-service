package ru.scala

import com.google.inject.Injector
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.reflect.{ClassTag, classTag}

package object learning extends PlayJsonSupport{
  private var injector: Injector = _

  def shareInjector(injector: Injector): Unit = {
    this.injector = injector
  }

  // Зачем ClassTag?
  // injector.getInstance принимает Class, но в рантайме у T эта информация утеряна из-за type erasure
  // T: ClassTag говорит компилятору, что нужно сгенерировать ClassTag для T, который будет содержать в рантайме эту инфу
  // (компилятор добавит implicit параметр в аргументы метода, и соответствующий implicit val рядом с вызовом метода)
  // Чтобы в рантайме получить Class типа T, используем функцию classTag[T].runtimeClass
  // (функция classTag принимает тот самый имплисит val, добавленный компилятором)
  def inject[T: ClassTag]: T = {
    injector.getInstance(classTag[T].runtimeClass.asInstanceOf[Class[T]])
  }

}
