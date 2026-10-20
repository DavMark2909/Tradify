import { useNavigate } from 'react-router-dom';
import './SellerActionPanel.css';

// TODO: fill in the route for each action
const ACTION_PATHS = {
  'View products': '/company-items',
  'View statistics': '/company-items',
  'Modify items': '/company-items',
};

const ACTIONS = Object.keys(ACTION_PATHS);

export default function SellerActionPanel({ className = '' }) {
  const navigate = useNavigate();

  const handleClick = (action) => {
    const path = ACTION_PATHS[action];
    if (path) navigate(path);
  };

  return (
    <div className={`seller-action-panel ${className}`}>
      <h2 className="seller-action-panel__title">Seller tools</h2>
      <ul className="seller-action-panel__list">
        {ACTIONS.map((action) => (
          <li key={action}>
            <button type="button" className="seller-action-panel__item" onClick={() => handleClick(action)}>
              {action}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
