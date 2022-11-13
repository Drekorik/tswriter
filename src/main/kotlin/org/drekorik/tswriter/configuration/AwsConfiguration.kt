package org.drekorik.tswriter.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.profiles.ProfileFile
import software.amazon.awssdk.regions.providers.AwsProfileRegionProvider
import software.amazon.awssdk.regions.providers.AwsRegionProvider
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain
import software.amazon.awssdk.services.timestreamwrite.TimestreamWriteClient

@Configuration
class AwsConfiguration(
    val environment: Environment
) {

    @Bean
    @Primary
    fun awsCredentialsProvider(
        @Value("\${aws.profile:default}") profileName: String,
    ): AwsCredentialsProvider {
        return DefaultCredentialsProvider.builder().profileName(profileName).build()
    }

    @Bean
    @Primary
    fun awsRegionProvider(@Value("\${aws.profile:default}") profileName: String): AwsRegionProvider {
        return if (ProfileFile.defaultProfileFile().profiles().containsKey(profileName) && profileName != "default") {
            AwsProfileRegionProvider(ProfileFile::defaultProfileFile, profileName)
        } else {
            DefaultAwsRegionProviderChain()
        }
    }

    @Bean
    fun timestreamWriteClient(
        awsRegionProvider: AwsRegionProvider,
        awsCredentialsProvider: AwsCredentialsProvider
    ): TimestreamWriteClient {
        return TimestreamWriteClient.builder()
            .credentialsProvider(awsCredentialsProvider)
            .region(awsRegionProvider.region)
            .build()
    }

}