import { createGlobalStyle } from "styled-components";
import reset from "styled-reset";

const GlobalStyle = createGlobalStyle`
    ${reset}
    
    html, body, #root{
        width: 100%;
        height:100%;
        font-family: Pretendard;
    }

    *{
        font-family: Pretendard;
    }
    
    button{
        background: inherit;
        border:none;
        box-shadow:none;
        border-radius:0;
        padding:0;
        overflow:visible;
        cursor:pointer
    }
`;

export default GlobalStyle;
