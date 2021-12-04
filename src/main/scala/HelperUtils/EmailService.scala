package HelperUtils

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider

import java.io.IOException
import org.apache.log4j.Logger
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.{AmazonSimpleEmailService, AmazonSimpleEmailServiceClient, AmazonSimpleEmailServiceClientBuilder}
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

class AwsEmailService
object AwsEmailService {
  def email(a: Int): Unit = {

  val log = Logger.getLogger(classOf[AwsEmailService])

  // Load configuration values from Application config
  val config = ConfigFactory.load("application.conf")


  val Source_EmailAddress : String = config.getString("emailServiceConfig.source_email")
  val DestinationList_EmailAddress: List[String] = List(config.getString("emailServiceConfig.addressList"))

  // The subject line for the email.
  val email_subject = config.getString("emailServiceConfig.email_subject")

  // The body for the email.
  val message_body: Body = new Body(new Content(config.getString("emailServiceConfig.message_body")+a.toString))
  log.info("Subject body added to Body")

  // destination email address
  val each_destination_email:Destination = new Destination(DestinationList_EmailAddress.asJava)
  log.info("Target address list added to destination")

  val message:Message =
    new Message(new Content(email_subject), message_body)

  /**
   * A handle on Simple Email Service with credentials fetched from the environment variables
   *
   *     AWS_ACCESS_KEY_ID
   *     AWS_SECRET_KEY
   */

  //@throws[IOException]

    try {
      val customer = AmazonSimpleEmailServiceClientBuilder.standard.withRegion(Regions.US_EAST_2).build()
      val request = new SendEmailRequest(Source_EmailAddress, each_destination_email, message)
      customer.sendEmail(request)
      log.info("Sending Email to the customers")
      System.out.println("Email is sent successfully")
    } catch {
      case exception: Exception =>
        log.error("Failed - Email not sent")
        System.out.println("Failed - Email not sent . Error message: " + exception.getMessage)
    }
  }
}
