import "../styles/globals.css";
import Layout from "../components/Layout";
import { useSSR } from '@nextui-org/react'


function MyApp({ Component, pageProps }) {
  const { isBrowser } = useSSR()
  return (
    <Layout>
      <Component {...pageProps} />
    </Layout>
  );
}

export default MyApp;