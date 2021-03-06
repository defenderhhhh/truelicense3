/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */

package org.truelicense.it.core

import java.util.Locale.ENGLISH

import org.scalatest.Matchers._
import org.scalatest._
import org.truelicense.spi.codec.SerializationCodec

/**
 * @author Christian Schlichtherle
 */
abstract class RepositoryTestSuite[Model <: AnyRef]
extends WordSpec with ParallelTestExecution { this: TestContext[Model] =>

  private val _codec = new SerializationCodec {
    override def contentType = super.contentType.toUpperCase(ENGLISH)
    override def contentTransferEncoding = super.contentTransferEncoding.toUpperCase(ENGLISH)
  }

  "A repository" should {
    "sign and verify an object" in {
      val authentication = vendorManager.parameters.authentication
      val context = repositoryContext
      val model = context.model()
      val controller = context.controller(model, _codec)
      val artifact = "Hello world!"
      (authentication sign (controller, artifact) decode classOf[String]).asInstanceOf[String] should be (artifact)
      (authentication verify controller decode classOf[String]).asInstanceOf[String] should be (artifact)
    }
  }
}
