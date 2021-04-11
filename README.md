<html lang="ja">
<head><meta charset="utf-8"/></head>
<body>
<h1>■ InterFAX REST サービス アクセスサンプル</h1>
このプロジェクトコードでは、InterFAX REST サービスへアクセスする為の Java サンプルプログラムを公開しています。
ご利用になられる場合は適宜自由に改修して頂いて構いませんが、ご利用者様の業務システムとあわせて業務上必要な要件に対する充分な動作検証を実施してください。
<div>
<h3>1. 前提とする環境</h3>
当該サンプルプログラムは、Java8の環境を基準に動作を確認しています。
<h3>2. アーキテクチャ</h3>
<h4>2-1. InterFAX RESTサービスの呼出</h4>
RESTサービスの呼出は、以下の5つのSTUB(クラス)に分類して実装されています。<br>
各STUBは、接続先情報を提供するConnectorクラスを使ってインスタンス化します。<br>
その後、アプリケーションは、各STUBで提供されるそれぞれのREST APIの呼出を行います。<br>
<img src="stub.png">
<h4>2-2. 戻り値</h4>
REST API の戻り値は、MethodResponseクラスが基本となりますが、必要に応じてAPI毎に継承を行っています。
例えば、過去のFAX送信リクエストの一覧を取得するような場合がそのケースです。<br>
<br>
いずれにしろREST API呼出の成否は、MethodResponse.isSuccess()によって確認を行います。<br>
尚、通信エラーなどの場合は、javax.ws.rs.ProcessingExceptionがスローされます。<br>
<img src="return.png">
<h3>3. REST APIの分類と呼出サンプル</h3>
REST APIの分類は、そのままSTUBとして分類されています。mainパッケージ内に、多数のサンプル呼出コードを提供しています。
これらを参考として頂ければ、呼出手順や結果の取得方法などがご理解頂けるかと存じます。
<h4>3-1. Addressbooks</h4>
「アカウント管理WEB」(https://secure.interfax.net) にてご利用者様が設定されている、リスト(Address Book)を取得できます。<br>
<br>
【mainパッケージ内のサンプルの呼出コード】
<table>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecGetAddressBooks.java">ExecGetAddressBooks</a></td>
<td>[アカウント管理WEB]にて登録済みの [リスト](AddressBooks)の一覧を取得</td>
</tr>
</table>
<h4>3-2. Outbound Credits</h4>
与信限度額に対する残高を確認できます。<br>
<br>
【mainパッケージ内のサンプルの呼出コード】
<table>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecOutboundCredits.java">ExecOutboundCredits</a></td>
<td>ユーザの与信限度額残高を取得</td>
</tr>
</table>
<h4>3-3. Receiving Faxes</h4>
受信したFAXへアクセスします。受信記録の一覧取得や、受信イメージの取得が行えます。<br>
<br>
【mainパッケージ内のサンプルの呼出コード】
<table>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecReceivingFaxesGetList.java">ExecReceivingFaxesGetList</a></td>
<td>受信済みFAX一覧のステータスを取得</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecReceivingFaxesGetImage.java">ExecReceivingFaxesGetImage</a></td>
<td>FAX受信イメージを取得</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecReceivingFaxesMark.java">ExecReceivingFaxesMark</a></td>
<td>特定の FAX受信に関し既読マーク設定</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecReceivingFaxesResend.java">ExecReceivingFaxesResend</a></td>
<td>特定の FAX受信に関する受信転送メールの再送を指示</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecReceivingFaxesGetFoprwardingEMails.java">ExecReceivingFaxesGetFoprwardingEMails</a></td>
<td>受信転送メールの送付記録を取得</td>
</tr>
</table>
<h4>3-4. Sending Faxes</h4>
FAX送信をリクエストします。1回のFAX送信要求で指定できる送付先は1つです。<br>
また、過去のFAX送信記録の一覧取得や、送信イメージの取得なども行えます<br>
<br>
【mainパッケージ内のサンプルの呼出コード】
<table>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxWithSingleDocument.java">ExecSendFaxWithSingleDocument</a></td>
<td>単一のFAXコンテンツを使ったFAX送信</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxWithSingleHtml.java">ExecSendFaxWithSingleHtml</a></td>
<td>単一のFAXコンテンツ (HTML) を使ったFAX送信</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxWithSingleHtmlLink.java">ExecSendFaxWithSingleHtmlLink</a></td>
<td>外部の HTML ページを使ったFAX送信</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxWithSingleText.java">ExecSendFaxWithSingleText</a></td>
<td>単一の Text コンテンツを使ったFAX送信</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxWithMultiDocuments.java">ExecSendFaxWithMultiDocuments</a></td>
<td>複数のFAXコンテンツを使ったFAX送信</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxWithMixedDocuments.java">ExecSendFaxWithMixedDocuments</a></td>
<td>テキスト及びアップロードコンテンツによるFAX送信</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxCancelFax.java">ExecSendFaxCancelFax</a></td>
<td>FAX送信のキャンセル</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxGetCompletedFaxList.java">ExecSendFaxGetCompletedFaxList</a></td>
<td>完了済み FAX送信の状態取得</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxGetFaxList.java">ExecSendFaxGetFaxList</a></td>
<td>FAX送信の一覧取得</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxGetFaxRecord.java">ExecSendFaxGetFaxRecord</a></td>
<td>特定のFAX送信の状態を取得</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxGetImage.java">ExecSendFaxGetImage</a></td>
<td>特定のFAX送信のイメージを取得</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxHideFax.java">ExecSendFaxHideFax</a></td>
<td>FAX送信の状態情報の秘匿化</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxResendFax.java">ExecSendFaxResendFax</a></td>
<td>完了済みFAX送信の再送付</td>
</tr>
<tr>
<td style="background-color: lightgray"><a href="src/main/java/main/ExecSendFaxSearchFaxList.java">ExecSendFaxSearchFaxList</a></td>
<td>FAX送信の記録の検索</td>
</tr>
</table>
</div>
</body>
</html>
