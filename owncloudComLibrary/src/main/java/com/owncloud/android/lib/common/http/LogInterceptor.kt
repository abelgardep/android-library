/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2020 ownCloud GmbH.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */
package com.owncloud.android.lib.common.http

import com.owncloud.android.lib.common.http.LogBuilder.toLogString
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Log request
        chain.request().let {
            Timber.d(toLogString(NetworkPetition.REQUEST, NetworkNode.INFO, "Type: ${it.method} URL: ${it.url}"))
            it.headers.forEach { header ->
                Timber.d(toLogString(NetworkPetition.REQUEST, NetworkNode.HEADER, header.toString()))
            }
            Timber.d(toLogString(NetworkPetition.REQUEST, NetworkNode.BODY, it.body.toString()))
        }

        val response = chain.proceed(chain.request())

        // Log response
        response.let {
            Timber.d(toLogString(NetworkPetition.RESPONSE, NetworkNode.INFO, "RequestId: ${it.request.header(HttpConstants.OC_X_REQUEST_ID)}"))
            Timber.d(toLogString(NetworkPetition.RESPONSE, NetworkNode.INFO, "Code: ${it.code}  Message: ${it.message} IsSuccessful: ${it.isSuccessful}"))
            it.headers.forEach { header ->
                Timber.d(toLogString(NetworkPetition.RESPONSE, NetworkNode.HEADER, header.toString()))
            }
            Timber.d(toLogString(NetworkPetition.RESPONSE, NetworkNode.BODY, it.body.toString()))
        }

        return response
    }
}
