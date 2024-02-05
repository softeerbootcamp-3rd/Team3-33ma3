import styled from "styled-components";

const Button = styled.button`
  color: ${(props) => props.theme.colors.text_default};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;

  padding: 5px 15px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-radius: ${(props) => props.theme.radiuses.radius_m};
  border: 1px solid ${(props) => props.theme.colors.border_strong};
  background: ${(props) => props.theme.colors.surface_default};

  &:hover,
  &.active {
    border: 1.5px solid ${(props) => props.theme.colors.surface_black};
    background: ${(props) => props.theme.colors.surface_default};
    color: ${(props) => props.theme.colors.text_strong};
  }
`;
