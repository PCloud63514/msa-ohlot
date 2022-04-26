/**
 * Next JS 랜더링 순서
 * _app.js -> index.js
 */
import "../styles/globals.css"
import Layout from "../components/Layout";

export default function App({Component, pageProps}) {
    return (
        <Layout>
            <Component {...pageProps}/>
        </Layout>
    )
}


