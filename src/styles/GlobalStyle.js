import { createGlobalStyle } from "styled-components";
import reset from "styled-reset";

const GlobalStyle = createGlobalStyle`
    ${reset}

    * {
        margin: 0;
        padding: 0;
        font-family: Pretendard;
        font-style: normal;
        line-height: normal;
    }

    html, body {
        width: 100%;
        height:100%;
    }
    
    button{
        background: inherit ;
        border:none;
        box-shadow:none;
        border-radius:0;
        padding:0;
        overflow:visible;
        cursor:pointer
    }
`;

export default GlobalStyle;
