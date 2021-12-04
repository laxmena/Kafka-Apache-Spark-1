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
  /**
   * This method sends email to list of destination email addresses
   * with number of error and warn logs.
   * @param errors - the number of error logs present
   * @param warn - the number of warn logs present
   */
  def email(errors: Int,warn :Int): Unit = {

  val log = Logger.getLogger(classOf[AwsEmailService])

  // Load configuration values from Application config
  val config = ConfigFactory.load("application.conf")


  val Source_EmailAddress : String = config.getString("emailServiceConfig.source_email")
  val DestinationList_EmailAddress: List[String] = List(config.getString("emailServiceConfig.addressList"))

  // The subject line for the email.
  val email_subject = config.getString("emailServiceConfig.email_subject")
  val msg = "\n Received "+errors.toString+" error logs and "+warn.toString+ " warn logs"
  // The body for the email.
  val message_body: Body = new Body(new Content(config.getString("emailServiceConfig.message_body")+msg))
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
      // Customer is the client here
      val customer = AmazonSimpleEmailServiceClientBuilder.standard.withRegion(Regions.US_EAST_2).build()
      /*Create an email request with parameters
      * source email
      * list of destination email
      * message to be sent */
      val request = new SendEmailRequest(Source_EmailAddress, each_destination_email, message)
      customer.sendEmail(request)
      log.info("Sending Email")

    } catch {
      case exception: Exception =>
        //If the email was not sent due to some error print the error
        log.error("Failed - Email not sent" + exception.getMessage)
    }
  }
}
